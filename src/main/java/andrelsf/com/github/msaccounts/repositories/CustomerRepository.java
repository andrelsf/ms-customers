package andrelsf.com.github.msaccounts.repositories;

import andrelsf.com.github.msaccounts.entities.CustomerEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {

  @Query(value = "SELECT ce FROM CustomerEntity AS ce "
      + "WHERE (:accountNumber IS NULL OR ce.account.accountNumber = :accountNumber)")
  List<CustomerEntity> findAll(@Param("accountNumber") Integer accountNumber, Pageable pageable);

}
