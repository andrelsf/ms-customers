package andrelsf.com.github.msaccounts.repositories;

import andrelsf.com.github.msaccounts.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

}
