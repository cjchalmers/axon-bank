package nl.simplexit.axon.event;

import lombok.Data;

/**
 * Created by Colin on 05/08/2016.
 */
@Data
public class AccountCreatedEvent {

  private  String accountNo;

  public AccountCreatedEvent(){};

  public AccountCreatedEvent(String accountNo) {
    this.accountNo = accountNo;
  }

  public String getAccountNo() {
    return accountNo;
  }

}
