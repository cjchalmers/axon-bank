package nl.simplexit.axon.event;

import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Created by Dadepo Aderemi.
 */
@Component
public class AccountDebitedEventHandler {

  @Autowired
  DataSource dataSource;

  @EventHandler
  public void handleAccountDebitedEvent(AccountDebitedEvent event) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // Get the current states as reflected in the event
    String accountNo = event.getAccountNo();
    Double balance = event.getBalance();
    Double amountDebited = event.getAmountDebited();
    Double newBalance = balance - amountDebited;

    // Update the view
    String updateQuery = "UPDATE account_view SET balance = ? WHERE account_no = ?";
    jdbcTemplate.update(updateQuery, new Object[]{newBalance, accountNo});
  }
}
