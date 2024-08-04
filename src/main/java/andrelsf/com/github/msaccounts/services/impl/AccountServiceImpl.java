package andrelsf.com.github.msaccounts.services.impl;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.entities.AccountEntity;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import andrelsf.com.github.msaccounts.entities.TransactionStatus;
import andrelsf.com.github.msaccounts.entities.TransferEntity;
import andrelsf.com.github.msaccounts.handlers.exceptions.AccountNotFoundException;
import andrelsf.com.github.msaccounts.handlers.exceptions.UnableToTransfer;
import andrelsf.com.github.msaccounts.repositories.AccountRepository;
import andrelsf.com.github.msaccounts.repositories.TransferRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final TransferRepository transferRepository;

  public AccountServiceImpl(AccountRepository accountRepository, TransferRepository transferRepository) {
    this.transferRepository = transferRepository;
    this.accountRepository = accountRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<AccountResponse> getAccounts(final Params params) {
    return accountRepository.findAll(AccountStatus.ACTIVE, params.accountNumber(), params.getPageable())
        .stream()
        .map(Mapper::toAccountResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public AccountResponse getTargetAccountBy(final UUID sourceAccountId, final PostTransferRequest request) {
    return accountRepository.findByAgencyAndAccountNumber(request.agency(), request.accountNumber())
        .map(Mapper::toAccountResponse)
        .orElseThrow(() -> {
          final TransferEntity transferEntity = TransferEntity.of(sourceAccountId, request, TransactionStatus.FAILED);
          transferRepository.save(transferEntity);
          return new UnableToTransfer(
              "Unable to initiate transfer.\n Target account not found by agency and account number.");
        });
  }

  @Override
  @Transactional
  public void processTransfer(String sourceAccountId, String targetAccountId, BigDecimal amount) {
    AccountEntity sourceAccount = accountRepository.findById(sourceAccountId).get();
    AccountEntity targetAccount = accountRepository.findById(targetAccountId).get();
    final ZonedDateTime transferDate = ZonedDateTime.now();
    sourceAccount.debit(amount, transferDate);
    targetAccount.credit(amount, transferDate);
  }

  @Override
  @Transactional
  public AccountResponse getAccountForTransfer(final UUID accountId, final PostTransferRequest request) {
    return accountRepository.findByIdAndBalanceGreaterThanEqual(accountId.toString(), request.amount())
        .map(Mapper::toAccountResponse)
        .orElseThrow(() -> {
          final TransferEntity transferEntity = TransferEntity.of(accountId, request, TransactionStatus.FAILED);
          transferRepository.save(transferEntity);
          return new UnableToTransfer(
              "Unable to initiate transfer.\n Please check your account balance and try again.");
        });
  }

  @Override
  @Transactional(readOnly = true)
  public AccountResponse findById(final UUID accountId) {
    return accountRepository.findById(accountId.toString())
        .map(Mapper::toAccountResponse)
        .orElseThrow(() ->
            new AccountNotFoundException("Account not found by accountId=".concat(accountId.toString())));
  }
}
