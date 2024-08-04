package andrelsf.com.github.msaccounts.api.resources;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.services.AccountService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/v1/accounts",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AccountResource {

  private final AccountService accountService;

  public AccountResource(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping
  public ResponseEntity<List<AccountResponse>> getAccounts(
      @RequestParam(value = "accountNumber", defaultValue = "") Integer accountNumber,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size) {
    final Params params = Params.of(accountNumber, page, size);
    final List<AccountResponse> accounts = accountService.getAccounts(params);
    return ResponseEntity.ok(accounts);
  }

  @GetMapping("/{accountId}")
  public ResponseEntity<AccountResponse> getAccountById(@PathVariable @NotNull final UUID accountId) {
    final AccountResponse accountResponse = accountService.findById(accountId);
    return ResponseEntity.ok(accountResponse);
  }
}
