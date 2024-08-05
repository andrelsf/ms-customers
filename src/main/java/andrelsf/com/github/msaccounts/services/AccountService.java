package andrelsf.com.github.msaccounts.services;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
  AccountResponse findById(final UUID accountId);
  List<AccountResponse> getAccounts(final Params params);
  void processTransfer(String sourceAccountId, String targetAccountId, BigDecimal amount);
  AccountResponse getAccountForTransfer(final UUID accountId, final PostTransferRequest request);
  AccountResponse getTargetAccountBy(final UUID sourceAccountId, final PostTransferRequest request);
}
