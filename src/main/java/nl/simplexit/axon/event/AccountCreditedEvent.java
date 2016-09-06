package nl.simplexit.axon.event;

/**
 * Created by Colin on 05/08/2016.
 */
public class AccountCreditedEvent {
  private  String accountNo;
  private  Double amountCredited;
  private  Double balance;

  public AccountCreditedEvent(String accountNo, Double amountCredited, Double balance) {
    this.accountNo = accountNo;
    this.amountCredited = amountCredited;
    this.balance = balance;
  }

  public AccountCreditedEvent(){};

  public String getAccountNo() {
    return accountNo;
  }

  public Double getAmountCredited() {
    return amountCredited;
  }

  public Double getBalance() {
    return balance;
  }
}
