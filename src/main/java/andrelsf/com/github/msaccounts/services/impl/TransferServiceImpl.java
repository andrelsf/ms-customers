package andrelsf.com.github.msaccounts.services.impl;

import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.api.http.responses.TransferResponse;
import andrelsf.com.github.msaccounts.entities.TransactionStatus;
import andrelsf.com.github.msaccounts.entities.TransferEntity;
import andrelsf.com.github.msaccounts.entities.TransferRecord;
import andrelsf.com.github.msaccounts.handlers.exceptions.UnableToTransferException;
import andrelsf.com.github.msaccounts.repositories.TransferRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.services.TransferService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferServiceImpl implements TransferService {

  private final static BigDecimal ALLOWED_TRANSFER_AMOUNT = new BigDecimal("10000.00");

  private final AccountService accountService;
  private final TransferRepository transferRepository;

  public TransferServiceImpl(AccountService accountService, TransferRepository transferRepository) {
    this.accountService = accountService;
    this.transferRepository = transferRepository;
  }

  @Override
  public TransferResponse doTransfer(final UUID sourceAccountId, final PostTransferRequest request) {
    try {
      if (request.amount().compareTo(ALLOWED_TRANSFER_AMOUNT) > 0) {
        throw new UnableToTransferException("Transfer amount not allowed");
      }

      final AccountResponse sourceAccount = accountService.getAccountForTransfer(sourceAccountId, request);
      final AccountResponse targetAccount = accountService.getTargetAccountBy(sourceAccountId, request);

      // TODO: transactionService.lock(sourceAccountId);
      accountService.processTransfer(sourceAccount.accountId(), targetAccount.accountId(), request.amount());
      // TODO: transactionSerivce.unlock(sourceAccountId);

      final TransferRecord transferRecord = TransferRecord.of(
          sourceAccountId, request, TransactionStatus.COMPLETED.name(), TransactionStatus.COMPLETED.getMessage());
      return this.registerTransfer(transferRecord);
    }
    catch (UnableToTransferException ex) {
      final TransferRecord transferRecord = TransferRecord.of(
          sourceAccountId, request, TransactionStatus.FAILED.name(), ex.getMessage());
      return this.registerTransfer(transferRecord);
    }
  }

  @Override
  public List<TransferResponse> getAllTransfers(final UUID accountId) {
    return transferRepository.findAllByAccountId(accountId.toString())
        .stream()
        .map(Mapper::toTransferResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public TransferResponse registerTransfer(final TransferRecord transferRecord) {
    final TransferEntity transferEntity = Mapper.recordToTransferEntity(transferRecord);
    final TransferEntity transfer = transferRepository.save(transferEntity);
    return Mapper.toTransferResponse(transfer);
  }
}
