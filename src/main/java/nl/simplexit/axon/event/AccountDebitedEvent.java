package nl.simplexit.axon.event;

/**
 * Created by Colin on 05/08/2016.
 */
public class AccountDebitedEvent {
  private String accountNo;
  private Double amountDebited;
  private Double balance;

  public AccountDebitedEvent(String accountNo, Double amountDebited, Double balance) {
    this.accountNo = accountNo;
    this.amountDebited = amountDebited;
    this.balance = balance;
  }

  public AccountDebitedEvent(){};

  public String getAccountNo() {
    return accountNo;
  }

  public Double getAmountDebited() {
    return amountDebited;
  }

  public Double getBalance() {
    return balance;
  }
}
