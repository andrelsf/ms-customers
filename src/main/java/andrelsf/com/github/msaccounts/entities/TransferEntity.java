package andrelsf.com.github.msaccounts.entities;

import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "transfers")
public class TransferEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "transfer_id", length = 36, nullable = false)
  private String id;

  @Column(length = 36, nullable = false)
  private String accountId;

  @Positive
  @Column(nullable = false)
  private Integer targetAgency;

  @Positive
  @Column(nullable = false)
  private Integer targetAccountNumber;

  @Column(nullable = false)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionStatus status;

  @CreationTimestamp
  private ZonedDateTime transferDate;

  public TransferEntity() {
  }

  public TransferEntity(
      String id, String accountId, Integer targetAgency, Integer targetAccountNumber,
      BigDecimal amount, TransactionStatus status, ZonedDateTime transferDate) {
    this.id = id;
    this.accountId = accountId;
    this.targetAgency = targetAgency;
    this.targetAccountNumber = targetAccountNumber;
    this.amount = amount;
    this.status = status;
    this.transferDate = transferDate;
  }

  public static TransferEntity of(
      final UUID accountId, final PostTransferRequest request, final TransactionStatus transactionStatus) {
    TransferEntity transferEntity = new TransferEntity();
    transferEntity.setAccountId(accountId.toString());
    transferEntity.setTargetAgency(request.agency());
    transferEntity.setTargetAccountNumber(request.accountNumber());
    transferEntity.setAmount(request.amount());
    transferEntity.setStatus(transactionStatus);
    transferEntity.setTransferDate(ZonedDateTime.now());
    return transferEntity;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String customerId) {
    this.accountId = customerId;
  }

  public Integer getTargetAgency() {
    return targetAgency;
  }

  public void setTargetAgency(Integer targetAgency) {
    this.targetAgency = targetAgency;
  }

  public Integer getTargetAccountNumber() {
    return targetAccountNumber;
  }

  public void setTargetAccountNumber(Integer targetAccountNumber) {
    this.targetAccountNumber = targetAccountNumber;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public TransactionStatus getStatus() {
    return status;
  }

  public void setStatus(TransactionStatus status) {
    this.status = status;
  }

  public ZonedDateTime getTransferDate() {
    return transferDate;
  }

  public void setTransferDate(ZonedDateTime transferDate) {
    this.transferDate = transferDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferEntity that = (TransferEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(accountId, that.accountId)
        && Objects.equals(targetAgency, that.targetAgency)
        && Objects.equals(targetAccountNumber, that.targetAccountNumber)
        && Objects.equals(amount, that.amount)
        && status == that.status
        && Objects.equals(transferDate, that.transferDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, accountId, targetAgency, targetAccountNumber, amount, status, transferDate);
  }
}