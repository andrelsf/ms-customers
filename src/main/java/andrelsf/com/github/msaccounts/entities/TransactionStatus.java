package andrelsf.com.github.msaccounts.entities;

public enum TransactionStatus {
  COMPLETED("Transfer completed successfully."),
  FAILED("Failed to perform transfer between accounts");

  private final String message;

  TransactionStatus(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
