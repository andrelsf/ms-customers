package andrelsf.com.github.msaccounts.handlers.exceptions;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(final String message) {
    super(message);
  }
}
