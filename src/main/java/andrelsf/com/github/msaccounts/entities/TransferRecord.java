package andrelsf.com.github.msaccounts.entities;

import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import java.math.BigDecimal;
import java.util.UUID;

public record TransferRecord(
   String accountId,
   Integer targetAgency,
   Integer targetAccountNumber,
   BigDecimal amount,
   String status
) {

  public static TransferRecord of(final UUID accountId, final PostTransferRequest request, final String status) {
    return new TransferRecord(
        accountId.toString(),
        request.agency(),
        request.accountNumber(),
        request.amount(),
        status);
  }
}
