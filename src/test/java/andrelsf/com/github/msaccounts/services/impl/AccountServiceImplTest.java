package andrelsf.com.github.msaccounts.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import andrelsf.com.github.msaccounts.api.http.requests.AccountRequest;
import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.entities.AccountEntity;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import andrelsf.com.github.msaccounts.handlers.exceptions.AccountNotFoundException;
import andrelsf.com.github.msaccounts.handlers.exceptions.UnableToTransferException;
import andrelsf.com.github.msaccounts.repositories.AccountRepository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  private AccountServiceImpl accountService;


  @Test
  @DisplayName("Deve realizar consulta para obter uma lista de todas as contas")
  void test_getAccounts_without_filter() {
    final Params params = Params.of("ACTIVE", null, 0, 10);
    final AccountEntity accountEntity = buildAccountEntity();

    when(accountRepository.findAll(AccountStatus.ACTIVE, params.accountNumber(), params.getPageable()))
        .thenReturn(List.of(accountEntity));

    final List<AccountResponse> accounts = accountService.getAccounts(params);

    assertThat(accounts)
        .isNotEmpty()
        .hasSize(1)
        .isInstanceOf(List.class);

    final AccountResponse accountResponse = accounts.get(0);

    assertThat(accountResponse)
        .isNotNull()
        .isInstanceOf(AccountResponse.class);
  }

  @Test
  @DisplayName("Dado ID valido ao consulta uma conta por ID deve retornar uma conta")
  void test_findById_id_valid() {
    final AccountEntity accountEntity = buildAccountEntity();
    final UUID accountId = UUID.fromString(accountEntity.getId());

    when(accountRepository.findById(accountId.toString()))
        .thenReturn(Optional.of(accountEntity));

    final AccountResponse accountResponse = accountService.findById(accountId);

    assertThat(accountResponse)
        .isNotNull()
        .isInstanceOf(AccountResponse.class);

    assertThat(accountResponse.accountId())
        .isNotBlank()
        .isEqualTo(accountId.toString());
  }

  @Test
  @DisplayName("Dado ID invalido ao consultar uma conta deve lancar uma exception AccountNotFound")
  void test_findById_throw_not_found_exception() {
    final UUID accountId = UUID.randomUUID();

    when(accountRepository.findById(accountId.toString()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> accountService.findById(accountId))
        .isInstanceOf(AccountNotFoundException.class)
        .hasMessage("Account not found by accountId=".concat(accountId.toString()));
  }

  @Test
  @DisplayName("Dado o ID da conta e a quantidade a ser transferida deve retornar a conta com saldo positivo")
  void test_getAccountForTransfer_account_with_balance_positive() {
    final AccountEntity accountEntity = buildAccountEntity();
    final UUID accountId = UUID.fromString(accountEntity.getId());
    final PostTransferRequest postTransferRequest = buildPostTransferRequest();

    when(accountRepository.findByIdAndBalanceGreaterThanEqual(accountId.toString(), postTransferRequest.amount()))
        .thenReturn(Optional.of(accountEntity));

    final AccountResponse accountResponse = accountService.getAccountForTransfer(accountId, postTransferRequest);

    assertThat(accountResponse)
        .isNotNull()
        .isInstanceOf(AccountResponse.class);
  }

  @Test
  @DisplayName("Dado o ID da conta valido e a quantidade a ser transferida, "
      + "nao lancar uma exception por nao ter saldo em conta.")
  void test_getAccountForTransfer_with_insufficient_balance_in_account() {
    final AccountEntity accountEntity = buildAccountEntity();
    final UUID accountId = UUID.fromString(accountEntity.getId());
    final PostTransferRequest postTransferRequest = buildPostTransferRequest();

    when(accountRepository.findByIdAndBalanceGreaterThanEqual(accountId.toString(), postTransferRequest.amount()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        accountService.getAccountForTransfer(accountId, postTransferRequest))
        .isInstanceOf(UnableToTransferException.class)
        .hasMessage("Unable to initiate transfer.\n Please check your account ID or balance and try again.");
  }

  @Test
  @DisplayName("Dado a conta de origem e destino validas, deve realizar a transferencia entre as contas")
  void test_processTransfer_success() {
    final AccountEntity sourceAccount = buildAccountEntity();
    final AccountEntity targetAccount = buildAccountEntity();
    final String sourceAccountId = sourceAccount.getId();
    final String targetAccountId = targetAccount.getId();
    final BigDecimal amount = new BigDecimal("100");

    when(accountRepository.findById(sourceAccountId))
        .thenReturn(Optional.of(sourceAccount));

    when(accountRepository.findById(targetAccountId))
        .thenReturn(Optional.of(targetAccount));

    accountService.processTransfer(sourceAccountId, targetAccountId, amount);

    verify(accountRepository, times(1)).findById(sourceAccountId);
    verify(accountRepository, times(1)).findById(targetAccountId);
  }

  @Test
  @DisplayName("Dado a conta de origem valida e destino invalida, deve parar a execao "
      + "e lancar uma exception")
  void test_processTransfer_fail() {
    final String targetAccountId = UUID.randomUUID().toString();
    final AccountEntity sourceAccount = buildAccountEntity();
    final String sourceAccountId = sourceAccount.getId();
    final BigDecimal amount = new BigDecimal("100");

    when(accountRepository.findById(sourceAccountId))
        .thenReturn(Optional.of(sourceAccount));

    when(accountRepository.findById(targetAccountId))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        accountService.processTransfer(sourceAccountId, targetAccountId, amount))
        .isInstanceOf(UnableToTransferException.class)
        .hasMessage("failed to process transfer");
  }

  @Test
  @DisplayName("Informado os dados para transferencia como agency e numbero da conta,"
      + " deve consultar a conta de destino e retorna-la")
  void test_getTargetAccountBy_agency_and_accountNumber() {
    final AccountEntity targetAccount = buildAccountEntity();
    final PostTransferRequest request = buildPostTransferRequest();

    when(accountRepository.findByAgencyAndAccountNumber(request.agency(), request.accountNumber()))
        .thenReturn(Optional.of(targetAccount));

    final AccountResponse accountResponse = accountService.getTargetAccountBy(
        request.agency(), request.accountNumber());

    assertThat(accountResponse)
        .isNotNull()
        .isInstanceOf(AccountResponse.class);
  }

  @Test
  @DisplayName("Informado os dados para transferencia, agencia e numero da conta."
      + " Nao deve retornar um conta e lancar uma exception.")
  void test_getTargetAccountBy_agency_and_accountNumber_empty() {
    final PostTransferRequest request = buildPostTransferRequest();

    when(accountRepository.findByAgencyAndAccountNumber(request.agency(), request.accountNumber()))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        accountService.getTargetAccountBy(request.agency(), request.accountNumber()))
        .isInstanceOf(UnableToTransferException.class)
        .hasMessage("Unable to initiate transfer.\n Target account not found by agency and account number.");
  }

  @Test
  @DisplayName("Deve registrar uma nova conta")
  void test_create() {
    final String accountId = UUID.randomUUID().toString();
    final AccountRequest accountRequest = new AccountRequest(1234, 4321);
    AccountEntity accountEntity = buildAccountEntity();
    accountEntity.setId(accountId);

    when(accountRepository.save(any(AccountEntity.class)))
        .thenReturn(accountEntity);

    final AccountResponse accountResponse = accountService.create(accountId, accountRequest);

    assertThat(accountResponse)
        .isNotNull()
        .isInstanceOf(AccountResponse.class);
  }

  private PostTransferRequest buildPostTransferRequest() {
    return new PostTransferRequest(1234, 4321, new BigDecimal("500"));
  }

  private AccountEntity buildAccountEntity() {
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setId(UUID.randomUUID().toString());
    accountEntity.setAgency(1234);
    accountEntity.setAccountNumber(100111);
    accountEntity.setBalance(new BigDecimal("1000.00"));
    accountEntity.setStatus(AccountStatus.ACTIVE);
    accountEntity.setCreatedAt(ZonedDateTime.now());
    accountEntity.setLastUpdated(ZonedDateTime.now());
    return accountEntity;
  }
}
