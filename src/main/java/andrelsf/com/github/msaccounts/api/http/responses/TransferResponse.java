package andrelsf.com.github.msaccounts.api.http.responses;

import andrelsf.com.github.msaccounts.entities.TransactionStatus;
import java.math.BigDecimal;

public record TransferResponse(
    String transferId,
    Integer targetAgency,
    Integer targetAccountNumber,
    BigDecimal amount,
    String status,
    String message,
    String transferDate
) {

  public boolean hasError() {
    return status.equalsIgnoreCase(TransactionStatus.FAILED.name());
  }
}
