import nl.simplexit.axon.command.CreditAccountCommand;
import nl.simplexit.axon.command.DebitAccountCommand;
import nl.simplexit.axon.event.AccountCreatedEvent;
import nl.simplexit.axon.event.AccountCreditedEvent;
import nl.simplexit.axon.event.AccountDebitedEvent;
import nl.simplexit.axon.handler.CreditAccountHandler;
import nl.simplexit.axon.handler.DebitAccountHandler;
import nl.simplexit.axon.model.Account;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = ExploringAxonApplication.class)
@WebAppConfiguration
public class ExploringAxonApplicationTests {

  private FixtureConfiguration fixture;
  private String accountNo;

  @Before
  public void setUp() {
    fixture = Fixtures.newGivenWhenThenFixture(Account.class);
    fixture.registerAnnotatedCommandHandler(new CreditAccountHandler(fixture.getRepository()))
      .registerAnnotatedCommandHandler(new DebitAccountHandler(fixture.getRepository()));
    accountNo = "test-acc";
  }

  @Test
  public void testFirstDeposit() {
    fixture.given(new AccountCreatedEvent(accountNo))
      .when(new CreditAccountCommand(accountNo, 100.00))
      .expectEvents(new AccountCreditedEvent(accountNo, 100.00, 0.0));

  }

  @Test
  public void testFirstSecondDeposit() {
    fixture.given(new AccountCreatedEvent(accountNo),
      new AccountCreditedEvent(accountNo, 100.00, 0.0))
      .when(new CreditAccountCommand(accountNo, 40.00))
      .expectEvents(new AccountCreditedEvent(accountNo, 40.00, 100.00));
  }

  @Test
  public void testCreditingDebitingAndCrediting() {
    fixture.given(new AccountCreatedEvent(accountNo),
      new AccountCreditedEvent(accountNo, 100.00, 0.0),
      new AccountDebitedEvent(accountNo, 40.00, 100.0))
      .when(new CreditAccountCommand(accountNo, 40.00))
      .expectEvents(new AccountCreditedEvent(accountNo, 40.00, 60.00));
  }

  @Test
  public void cannotCreditWithAMoreThanMillion() {
    fixture.given(new AccountCreatedEvent(accountNo))
      .when(new CreditAccountCommand(accountNo, 10000000.00))
      .expectException(IllegalArgumentException.class);
  }

  @Test
  public void cannotDebitAccountWithZeroBalance() {
    fixture.given(new AccountCreatedEvent(accountNo))
      .when(new DebitAccountCommand(accountNo, 1.0))
      .expectException(IllegalArgumentException.class);
  }

}
