package andrelsf.com.github.msaccounts.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "accounts")
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "account_id", length = 36, nullable = false)
  private String id;

  @Column(nullable = false)
  private Integer agency;

  @Column(name = "account_number", nullable = false)
  private Integer accountNumber;

  @Enumerated(EnumType.STRING)
  @Column(length = 9, nullable = false)
  private AccountStatus status;

  @Column(nullable = false)
  private BigDecimal balance;

  @CreationTimestamp
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  private ZonedDateTime lastUpdated;

  public AccountEntity() {
  }

  public AccountEntity(
      String id, Integer agency, Integer accountNumber,
      AccountStatus status, BigDecimal balance, ZonedDateTime createdAt,
      ZonedDateTime lastUpdated) {
    this.id = id;
    this.agency = agency;
    this.accountNumber = accountNumber;
    this.status = status;
    this.balance = balance;
    this.createdAt = createdAt;
    this.lastUpdated = lastUpdated;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getAgency() {
    return agency;
  }

  public void setAgency(Integer agency) {
    this.agency = agency;
  }

  public Integer getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(Integer accountNumber) {
    this.accountNumber = accountNumber;
  }

  public AccountStatus getStatus() {
    return status;
  }

  public void setStatus(AccountStatus status) {
    this.status = status;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ZonedDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(ZonedDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

}
