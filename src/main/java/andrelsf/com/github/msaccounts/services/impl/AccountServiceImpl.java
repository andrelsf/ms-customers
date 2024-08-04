package andrelsf.com.github.msaccounts.services.impl;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import andrelsf.com.github.msaccounts.handlers.exceptions.AccountNotFoundException;
import andrelsf.com.github.msaccounts.repositories.AccountRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {

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
  public AccountResponse findById(final UUID accountId) {
    return accountRepository.findById(accountId.toString())
        .map(Mapper::toAccountResponse)
        .orElseThrow(() ->
            new AccountNotFoundException("Account not found by accountId=".concat(accountId.toString())));
  }
}
