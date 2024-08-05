package andrelsf.com.github.msaccounts.utils;

import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.api.http.responses.TransferResponse;
import andrelsf.com.github.msaccounts.entities.AccountEntity;
import andrelsf.com.github.msaccounts.entities.TransactionStatus;
import andrelsf.com.github.msaccounts.entities.TransferEntity;
import andrelsf.com.github.msaccounts.entities.TransferRecord;
import java.time.ZonedDateTime;

public class Mapper {

  private Mapper() {}


  public static AccountResponse toAccountResponse(AccountEntity accountEntity) {
    return new AccountResponse(
        accountEntity.getId(),
        accountEntity.getAgency(),
        accountEntity.getAccountNumber(),
        accountEntity.getStatus().name(),
        accountEntity.getBalance(),
        accountEntity.getCreatedAt().toString(),
        accountEntity.getLastUpdated().toString());
  }

  public static TransferResponse toTransferResponse(final TransferEntity transfer) {
    return new TransferResponse(
        transfer.getId(),
        transfer.getTargetAgency(),
        transfer.getTargetAccountNumber(),
        transfer.getAmount(),
        transfer.getStatus().name(),
        transfer.getMessage(),
        transfer.getTransferDate().toString());
  }

  public static TransferEntity recordToTransferEntity(final TransferRecord transferRecord) {
    TransferEntity transferEntity = new TransferEntity();
    transferEntity.setAccountId(transferRecord.accountId());
    transferEntity.setTargetAgency(transferRecord.targetAgency());
    transferEntity.setTargetAccountNumber(transferRecord.targetAccountNumber());
    transferEntity.setAmount(transferRecord.amount());
    transferEntity.setStatus(TransactionStatus.valueOf(transferRecord.status()));
    transferEntity.setMessage(transferRecord.message());
    transferEntity.setTransferDate(ZonedDateTime.now());
    return transferEntity;
  }
}
