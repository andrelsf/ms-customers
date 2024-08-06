package andrelsf.com.github.msaccounts.services;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostCustomerRequest;
import andrelsf.com.github.msaccounts.api.http.responses.CustomerResponse;
import java.util.List;

public interface CustomerService {
  CustomerResponse findById(final String clientId);
  List<CustomerResponse> getAll(final Params params);
  CustomerResponse create(final PostCustomerRequest clientRequest);
}
