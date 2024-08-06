package andrelsf.com.github.msaccounts.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "accounts")
public class AccountEntity {

  @Id
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

  @OneToOne(mappedBy = "account")
  private CustomerEntity customer;
  @CreationTimestamp
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  private ZonedDateTime lastUpdated;

  public AccountEntity() {
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

  public void debit(final BigDecimal amount, ZonedDateTime transferDate) {
    this.balance = this.balance.subtract(amount);
    this.setLastUpdated(transferDate);
  }

  public void credit(BigDecimal amount, ZonedDateTime transferDate) {
    this.balance = this.balance.add(amount);
    this.setLastUpdated(transferDate);
  }

  public void fillWith(String accountId, Integer agency, Integer accountNumber) {
    this.setId(accountId);
    this.setAgency(agency);
    this.setAccountNumber(accountNumber);
    this.setStatus(AccountStatus.ACTIVE);
    this.setBalance(new BigDecimal("0.0"));
    this.setCreatedAt(ZonedDateTime.now());
    this.setLastUpdated(ZonedDateTime.now());
  }
}
