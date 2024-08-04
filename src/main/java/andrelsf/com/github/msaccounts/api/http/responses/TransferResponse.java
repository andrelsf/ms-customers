package andrelsf.com.github.msaccounts.api.http.responses;

import java.math.BigDecimal;

public record TransferResponse(
    String transferId,
    Integer targetAgency,
    Integer targetAccountNumber,
    BigDecimal amount,
    String status,
    String transferDate
) {

}
