package andrelsf.com.github.msaccounts.services;

import java.util.UUID;

public interface TransactionService {

  void lock(UUID accountId);
  void unlock(UUID accountId);

}
