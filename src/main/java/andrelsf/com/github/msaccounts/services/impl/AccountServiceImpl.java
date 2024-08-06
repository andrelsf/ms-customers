package andrelsf.com.github.msaccounts.services.impl;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.entities.AccountEntity;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import andrelsf.com.github.msaccounts.handlers.exceptions.AccountNotFoundException;
import andrelsf.com.github.msaccounts.handlers.exceptions.UnableToTransferException;
import andrelsf.com.github.msaccounts.repositories.AccountRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

  private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

  private final AccountRepository accountRepository;

  public AccountServiceImpl(AccountRepository accountRepository) {
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
  @Transactional(readOnly = true)
  public AccountResponse getTargetAccountBy(final Integer agency, final Integer accountNumber) {
    return accountRepository.findByAgencyAndAccountNumber(agency, accountNumber)
        .map(Mapper::toAccountResponse)
        .orElseThrow(() -> new UnableToTransferException(
              "Unable to initiate transfer.\n Target account not found by agency and account number."));
  }

  @Override
  @Transactional
  public void processTransfer(String sourceAccountId, String targetAccountId, BigDecimal amount) {
    try {
      AccountEntity sourceAccount = accountRepository.findById(sourceAccountId).get();
      AccountEntity targetAccount = accountRepository.findById(targetAccountId).get();
      final ZonedDateTime transferDate = ZonedDateTime.now();
      sourceAccount.debit(amount, transferDate);
      targetAccount.credit(amount, transferDate);
    } catch (RuntimeException ex) {
      log.error(ex.getMessage(), ex);
      throw new UnableToTransferException("failed to process transfer");
    }
  }

  @Override
  @Transactional
  public AccountResponse getAccountForTransfer(final UUID accountId, final PostTransferRequest request) {
    return accountRepository.findByIdAndBalanceGreaterThanEqual(accountId.toString(), request.amount())
        .map(Mapper::toAccountResponse)
        .orElseThrow(() ->
          new UnableToTransferException(
              "Unable to initiate transfer.\n Please check your account ID or balance and try again."));
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
