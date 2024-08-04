package andrelsf.com.github.msaccounts.api.resources;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.api.http.responses.TransferResponse;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.services.TransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(
    value = "/v1/accounts",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class AccountResource {

  private final AccountService accountService;
  private final TransferService transferService;

  public AccountResource(AccountService accountService, TransferService transferService) {
    this.accountService = accountService;
    this.transferService = transferService;
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

  @GetMapping("/{accountId}/transfers")
  public ResponseEntity<List<TransferResponse>> getTransfers(@PathVariable @NotNull final UUID accountId) {
    final List<TransferResponse> transfers = transferService.getAllTransfers(accountId);
    return ResponseEntity.ok(transfers);
  }

  @PostMapping("/{accountId}/transfers")
  public ResponseEntity<TransferResponse> postDoTransfer(
      @PathVariable @NotNull final UUID accountId,
      @RequestBody @Valid final PostTransferRequest postTransferRequest) {
    final UUID transferId = transferService.doTransfer(accountId, postTransferRequest);
    final URI uriLocation = UriComponentsBuilder.fromUriString("/api/v1/accounts/{accountId}/transfers/{transferId}")
        .buildAndExpand(accountId.toString(), transferId.toString())
        .toUri();
    return ResponseEntity.created(uriLocation).build();
  }
}
