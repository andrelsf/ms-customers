package andrelsf.com.github.msaccounts.api.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostCustomerRequest;
import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.CustomerResponse;
import andrelsf.com.github.msaccounts.api.http.responses.TransferResponse;
import andrelsf.com.github.msaccounts.services.CustomerService;
import andrelsf.com.github.msaccounts.services.TransferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(
  value = "/v1/customers",
  consumes = APPLICATION_JSON_VALUE,
  produces = APPLICATION_JSON_VALUE)
public class CustomerResource {

  private final CustomerService customerService;
  private final TransferService transferService;

  public CustomerResource(CustomerService customerService, TransferService transferService) {
    this.customerService = customerService;
    this.transferService = transferService;
  }

  @PostMapping
  public ResponseEntity<Void> postCreate(@RequestBody @Valid final PostCustomerRequest postCustomerRequest) {
    final String customerId = customerService.create(postCustomerRequest);
    final URI uriLocation = UriComponentsBuilder.fromUriString("/api/v1/customers/{customerId}")
        .buildAndExpand(customerId)
        .toUri();
    return ResponseEntity.created(uriLocation).build();
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponse>> getAll(
      @RequestParam(value = "status", defaultValue = "ACTIVE") String status,
      @RequestParam(value = "accountNumber", defaultValue = "") Integer accountNumber,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size) {
    final List<CustomerResponse> clients = customerService.getAll(Params.of(status, accountNumber, page, size));
    return ResponseEntity.ok(clients);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerResponse> getById(@PathVariable @NotNull final UUID customerId) {
    final CustomerResponse clientResponse = customerService.findById(customerId.toString());
    return ResponseEntity.ok(clientResponse);
  }

  @DeleteMapping("/{customerId}")
  public ResponseEntity<Void> inactiveById(@PathVariable @NotNull final UUID customerId) {
    customerService.inactivateCustomer(customerId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{customerId}")
  public ResponseEntity<Void> activeById(@PathVariable @NotNull final UUID customerId) {
    customerService.activateCustomer(customerId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{customerId}/transfers")
  public ResponseEntity<List<TransferResponse>> getTransfers(@PathVariable @NotNull final UUID customerId) {
    final List<TransferResponse> transfers = transferService.getAllTransfers(customerId);
    return ResponseEntity.ok(transfers);
  }

  @PostMapping("/{customerId}/transfers")
  public ResponseEntity<TransferResponse> postDoTransfer(
      @PathVariable @NotNull final UUID customerId,
      @RequestBody @Valid final PostTransferRequest postTransferRequest) {
    final TransferResponse transferResponse = transferService.doTransfer(customerId, postTransferRequest);
    final URI uriLocation = UriComponentsBuilder.fromUriString("/api/v1/customers/{customerId}/transfers/{transferId}")
        .buildAndExpand(customerId.toString(), transferResponse.transferId())
        .toUri();

    if (transferResponse.hasError()) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
          .location(uriLocation)
          .body(transferResponse);
    }

    return ResponseEntity.status(HttpStatus.OK)
        .location(uriLocation)
        .body(transferResponse);
  }
}
