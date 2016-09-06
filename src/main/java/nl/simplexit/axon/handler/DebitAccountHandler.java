package nl.simplexit.axon.handler;

import nl.simplexit.axon.command.DebitAccountCommand;
import nl.simplexit.axon.model.Account;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Colin on 06/08/2016.
 */
@Component
public class DebitAccountHandler {
  private Repository repository;

  @Autowired
  public DebitAccountHandler(Repository repository) {
    this.repository = repository;
  }

  @CommandHandler
  public void handle(DebitAccountCommand debitAccountCommandCommand){
    Account accountToDebit = (Account) repository.load(debitAccountCommandCommand.getAccount());
    accountToDebit.debit(debitAccountCommandCommand.getAmount());
  }
}
