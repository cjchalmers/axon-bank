package nl.simplexit.axon.command;

/**
 * Created by Colin on 06/08/2016.
 */
public class DebitAccountCommand {
  private final String account;
  private final Double amount;

  public DebitAccountCommand(String account, Double amount) {
    this.account = account;
    this.amount = amount;
  }

  public String getAccount() {
    return account;
  }

  public Double getAmount() {
    return amount;
  }
}
