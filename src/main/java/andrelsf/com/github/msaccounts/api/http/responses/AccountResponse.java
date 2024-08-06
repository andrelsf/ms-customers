package andrelsf.com.github.msaccounts.api.http.responses;

import java.math.BigDecimal;

public record AccountResponse(
  String accountId,
  Integer agency,
  Integer accountNumber,
  String status,
  BigDecimal balance,
  String createdAt,
  String lastUpdated
) {}
