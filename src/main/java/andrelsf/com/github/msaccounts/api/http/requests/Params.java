package andrelsf.com.github.msaccounts.api.http.requests;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record Params(Integer page, Integer size, Integer accountNumber) {

  public static Params of(Integer accountNumber, Integer page, Integer size) {
    return new Params(page, size, accountNumber);
  }

  public Pageable getPageable() {
    return PageRequest.of(page, size);
  }

  @Override
  public Integer accountNumber() {
    return ObjectUtils.defaultIfNull(this.accountNumber, null);
  }
}
