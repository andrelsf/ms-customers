package andrelsf.com.github.msaccounts.services;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import java.util.List;
import java.util.UUID;

public interface AccountService {

  List<AccountResponse> getAccounts(final Params params);
  AccountResponse findByByCustomerId(final UUID customerId);

}
