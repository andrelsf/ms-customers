package andrelsf.com.github.msaccounts.services.impl;

import andrelsf.com.github.msaccounts.entities.TransactionEntity;
import andrelsf.com.github.msaccounts.handlers.exceptions.UnableToTransferException;
import andrelsf.com.github.msaccounts.repositories.TransactionRepository;
import andrelsf.com.github.msaccounts.services.TransactionService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {

  private final static Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

  private final TransactionRepository transactionRepository;

  public TransactionServiceImpl(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Override
  @Transactional
  public void lock(final UUID accountId) {
    try {
      final TransactionEntity transactionEntity = new TransactionEntity(accountId.toString());
      transactionRepository.save(transactionEntity);
      log.info("Lock for transfer accountId=".concat(accountId.toString()));
    } catch (RuntimeException ex) {
      log.error(ex.getMessage(), ex);
      throw new UnableToTransferException("There is a transaction in progress");
    }
  }

  @Override
  @Transactional
  public void unlock(final UUID accountId) {
    transactionRepository.findById(accountId.toString())
        .ifPresent(transactionRepository::delete);
    log.info("Unlock accountId=".concat(accountId.toString()));
  }
}
