package nl.simplexit.axon.handler;

import nl.simplexit.axon.command.CreditAccountCommand;
import nl.simplexit.axon.model.Account;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Colin on 06/08/2016.
 */
@Component
public class CreditAccountHandler {

  private Repository repository;

  @Autowired
  public CreditAccountHandler(Repository repository) {
    this.repository = repository;
  }

  @CommandHandler
  public void handle(CreditAccountCommand creditAccountCommandCommand){
    Account accountToCredit = (Account) repository.load(creditAccountCommandCommand.getAccount());
    accountToCredit.credit(creditAccountCommandCommand.getAmount());
  }

}
