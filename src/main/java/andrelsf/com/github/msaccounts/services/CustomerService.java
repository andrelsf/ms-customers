package andrelsf.com.github.msaccounts.services;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostCustomerRequest;
import andrelsf.com.github.msaccounts.api.http.responses.CustomerResponse;
import java.util.List;
import java.util.UUID;

public interface CustomerService {
  void inactivateCustomer(final UUID customerId);
  CustomerResponse findById(final String clientId);
  List<CustomerResponse> getAll(final Params params);
  String create(final PostCustomerRequest clientRequest);
}
