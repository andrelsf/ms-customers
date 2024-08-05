package andrelsf.com.github.msaccounts.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

  @Id
  @Column(name = "transaction_id", length = 36, nullable = false)
  private String id;

  public TransactionEntity() {
  }

  public TransactionEntity(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransactionEntity that = (TransactionEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
