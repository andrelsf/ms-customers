package andrelsf.com.github.msaccounts.utils;

import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.entities.AccountEntity;

public class Mapper {

  private Mapper() {}


  public static AccountResponse toAccountResponse(AccountEntity accountEntity) {
    return new AccountResponse(
        accountEntity.getId(),
        accountEntity.getCustomerId(),
        accountEntity.getAgency(),
        accountEntity.getAccountNumber(),
        accountEntity.getStatus().name(),
        accountEntity.getBalance(),
        accountEntity.getCreatedAt().toString(),
        accountEntity.getLastUpdated().toString());
  }
}
