package andrelsf.com.github.msaccounts.api.resources;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostCustomerRequest;
import andrelsf.com.github.msaccounts.api.http.responses.CustomerResponse;
import andrelsf.com.github.msaccounts.services.CustomerService;
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
  value = "/v1/customers",
  consumes = MediaType.APPLICATION_JSON_VALUE,
  produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

  private final CustomerService customerService;

  public CustomerResource(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  public ResponseEntity<Void> postCreate(@RequestBody @Valid final PostCustomerRequest postCustomerRequest) {
    final CustomerResponse customer = customerService.create(postCustomerRequest);
    final URI uriLocation = UriComponentsBuilder.fromUriString("/api/v1/customers/{customerId}")
        .buildAndExpand(customer.customerId())
        .toUri();
    return ResponseEntity.created(uriLocation).build();
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponse>> getAll(
      @RequestParam(value = "accountNumber", defaultValue = "") Integer accountNumber,
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "10") final Integer size) {
    final List<CustomerResponse> clients = customerService.getAll(Params.of(accountNumber, page, size));
    return ResponseEntity.ok(clients);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerResponse> getById(@PathVariable @NotNull final UUID customerId) {
    final CustomerResponse clientResponse = customerService.findById(customerId.toString());
    return ResponseEntity.ok(clientResponse);
  }
}
