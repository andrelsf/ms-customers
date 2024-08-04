package andrelsf.com.github.msaccounts.repositories;

import andrelsf.com.github.msaccounts.entities.AccountEntity;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {

  @Query(value = "SELECT ac FROM AccountEntity AS ac "
      + "WHERE ac.status = :status "
      + "AND (:accountNumber IS NULL OR ac.accountNumber = :accountNumber)")
  List<AccountEntity> findAll(
      @Param("status") AccountStatus status,
      @Param("accountNumber") Integer accountNumber,
      Pageable pageable);

  Optional<AccountEntity> findByCustomerId(String string);

}
