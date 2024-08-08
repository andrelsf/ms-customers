package andrelsf.com.github.msaccounts.api.http.requests;

import andrelsf.com.github.msaccounts.entities.AccountStatus;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record Params(Integer page, Integer size, Integer accountNumber, AccountStatus status) {

  public static Params of(String status, Integer accountNumber, Integer page, Integer size) {
    return new Params(page, size, accountNumber, AccountStatus.valueOf(status));
  }

  public Pageable getPageable() {
    return PageRequest.of(page, size);
  }

  @Override
  public Integer accountNumber() {
    return ObjectUtils.defaultIfNull(this.accountNumber, null);
  }
}
