package andrelsf.com.github.msaccounts.handlers.exceptions;

public class CustomerNotFoundException extends RuntimeException {

  public static String errorMessage = "Customer not found by ID=";

  public CustomerNotFoundException(String message) {
    super(message);
  }
}
