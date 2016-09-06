package nl.simplexit.axon;

import nl.simplexit.axon.model.Account;
import org.axonframework.repository.Repository;
import org.axonframework.unitofwork.DefaultUnitOfWork;
import org.axonframework.unitofwork.UnitOfWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.FileSystemUtils;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;

@Component
public class Db {

  @Autowired
  @Qualifier("transactionManager")
  protected PlatformTransactionManager txManager;

  @Autowired
  private Repository repository;

  @Autowired
  private javax.sql.DataSource dataSource;

  @PostConstruct
  private void init(){
    // init the event store

    // delete previous events on startup
    FileSystemUtils.deleteRecursively(Paths.get("./events").toFile());

    TransactionTemplate transactionTmp = new TransactionTemplate(txManager);
    transactionTmp.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {

        try {
          UnitOfWork uow = DefaultUnitOfWork.startAndGet();
          repository.add(new Account("acc-one"));
          repository.add(new Account("acc-two"));
          uow.commit();
        } catch (Exception e){}
      }
    });

    // init the tables for query/view
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    try {
      jdbcTemplate.execute("create table account_view (account_no VARCHAR(10) not null, balance FLOAT) ");

      jdbcTemplate.update("insert into account_view (account_no, balance) values (?, ?)", new Object[]{"acc-one", 0.0});
      jdbcTemplate.update("insert into account_view (account_no, balance) values (?, ?)", new Object[]{"acc-two", 0.0});
    } catch (Exception e){}
  }

}
