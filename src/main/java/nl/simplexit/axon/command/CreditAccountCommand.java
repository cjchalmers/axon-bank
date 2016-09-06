package nl.simplexit.axon.command;

/**
 * Created by Colin on 06/08/2016.
 */
public class CreditAccountCommand {
  private final String account;
  private final Double amount;

  public CreditAccountCommand(String account, Double amount) {
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
