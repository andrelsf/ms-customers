package andrelsf.com.github.msaccounts.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.api.http.responses.TransferResponse;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import andrelsf.com.github.msaccounts.entities.TransactionStatus;
import andrelsf.com.github.msaccounts.entities.TransferEntity;
import andrelsf.com.github.msaccounts.entities.TransferRecord;
import andrelsf.com.github.msaccounts.repositories.TransferRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.services.TransactionService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

  @Mock
  private AccountService accountService;

  @Mock
  private TransferRepository transferRepository;

  @Mock
  private TransactionService transactionService;

  @InjectMocks
  private TransferServiceImpl transferService;

  @Test
  @DisplayName("Dada o ID da conta de origem e os dados para transferencia. "
      + "Deve retornar status da transacao com falha")
  void test_doTransfer_thrown_exception_transfer_amount_not_allowed() {
    final UUID sourceAccountId = UUID.randomUUID();
    final PostTransferRequest postTransferRequest = buildPostTransferRequest(new BigDecimal("10000.01"));
    final TransferRecord transferRecord = TransferRecord.of(
        sourceAccountId, postTransferRequest, TransactionStatus.FAILED.name(), "Transfer amount not allowed");
    final TransferEntity transferEntity = Mapper.recordToTransferEntity(transferRecord);

    when(transferRepository.save(any(TransferEntity.class)))
        .thenReturn(transferEntity);

    final TransferResponse transferResponse = transferService.doTransfer(sourceAccountId, postTransferRequest);

    assertThat(transferResponse)
        .isNotNull()
        .isInstanceOf(TransferResponse.class);

    assertThat(transferResponse.message())
        .isNotBlank()
        .isEqualTo("Transfer amount not allowed");
  }

  @Test
  @DisplayName("Dada o ID da conta de origem e os dados para transferencia."
      + " Deve retornar status da transacao com sucesso.")
  void test_doTransfer_success() {
    final UUID sourceAccountId = UUID.randomUUID();
    final UUID targetAccountId = UUID.randomUUID();
    final PostTransferRequest postTransferRequest = buildPostTransferRequest(new BigDecimal("1000.00"));
    final TransferRecord transferRecord = TransferRecord.of(
        sourceAccountId,
        postTransferRequest,
        TransactionStatus.COMPLETED.name(),
        TransactionStatus.COMPLETED.getMessage());
    final TransferEntity transferEntity = Mapper.recordToTransferEntity(transferRecord);
    final AccountResponse sourceAccount = buildAccountResponse(sourceAccountId);
    final AccountResponse targetAccount = buildAccountResponse(targetAccountId);

    when(accountService.getAccountForTransfer(sourceAccountId, postTransferRequest))
        .thenReturn(sourceAccount);
    when(accountService.getTargetAccountBy(
        postTransferRequest.agency(), postTransferRequest.accountNumber()))
        .thenReturn(targetAccount);
    doNothing()
        .when(transactionService).lock(sourceAccountId);
    doNothing()
        .when(accountService).processTransfer(
            sourceAccount.accountId(), targetAccount.accountId(), postTransferRequest.amount());
    when(transferRepository.save(any(TransferEntity.class)))
        .thenReturn(transferEntity);

    final TransferResponse transferResponse = transferService.doTransfer(sourceAccountId, postTransferRequest);

    assertThat(transferResponse)
        .isNotNull()
        .isInstanceOf(TransferResponse.class);

    assertThat(transferResponse.message())
        .isNotBlank()
        .isEqualTo("Transfer completed successfully.");
  }

  @Test
  @DisplayName("Dado o ID de uma conta deve listar todas as transferencias realizadas")
  void test_getAllTransfers_by_accountId() {
    final UUID accountId = UUID.randomUUID();
    final TransferEntity transferEntity = buildTransferEntity(accountId);

    when(transferRepository.findAllByAccountId(accountId.toString()))
        .thenReturn(List.of(transferEntity));

    final List<TransferResponse> transfers = transferService.getAllTransfers(accountId);

    assertThat(transfers)
        .isInstanceOf(List.class)
        .isNotEmpty()
        .hasSize(1);
  }

  private TransferEntity buildTransferEntity(UUID accountId) {
    return new TransferEntity(
        UUID.randomUUID().toString(),
        accountId.toString(),
        1234,
        4321,
        new BigDecimal("1000.00"),
        TransactionStatus.COMPLETED,
        TransactionStatus.COMPLETED.getMessage(),
        ZonedDateTime.now());
  }

  private AccountResponse buildAccountResponse(UUID sourceAccountId) {
    return new AccountResponse(
        sourceAccountId.toString(),
        1234,
        4321,
        AccountStatus.ACTIVE.name(),
        new BigDecimal("10000.00"),
        ZonedDateTime.now().toString(),
        ZonedDateTime.now().toString());
  }

  private PostTransferRequest buildPostTransferRequest(BigDecimal amount) {
    return new PostTransferRequest(1234, 4321, amount);
  }
}
