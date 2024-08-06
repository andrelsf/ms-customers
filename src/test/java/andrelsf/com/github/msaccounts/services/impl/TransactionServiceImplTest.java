package andrelsf.com.github.msaccounts.services.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import andrelsf.com.github.msaccounts.entities.TransactionEntity;
import andrelsf.com.github.msaccounts.repositories.TransactionRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransactionServiceImpl transactionService;

  @Test
  @DisplayName("Dado ID da conta valido deve ser registrado para inicar a transacao")
  void test_lock_success() {
    final UUID accountId = UUID.randomUUID();
    final TransactionEntity transactionEntity = new TransactionEntity(accountId.toString());

    when(transactionRepository.save(transactionEntity))
        .thenReturn(transactionEntity);

    transactionService.lock(accountId);

    verify(transactionRepository, times(1)).save(transactionEntity);
  }

  @Test
  @DisplayName("Dado ID da conta dever ser consultado e removido caso esteja presente")
  void test_unlock() {
    final UUID accountId = UUID.randomUUID();
    final TransactionEntity transactionEntity = new TransactionEntity(accountId.toString());

    when(transactionRepository.findById(accountId.toString()))
        .thenReturn(Optional.of(transactionEntity));
    doNothing()
        .when(transactionRepository).delete(transactionEntity);

    transactionService.unlock(accountId);

    verify(transactionRepository, times(1))
        .findById(accountId.toString());
    verify(transactionRepository, times(1))
        .delete(transactionEntity);
  }
}
