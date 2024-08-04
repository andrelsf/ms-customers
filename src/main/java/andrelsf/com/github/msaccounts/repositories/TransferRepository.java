package andrelsf.com.github.msaccounts.repositories;

import andrelsf.com.github.msaccounts.entities.TransferEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, String> {
  List<TransferEntity> findAllByAccountId(String accountId);
}
