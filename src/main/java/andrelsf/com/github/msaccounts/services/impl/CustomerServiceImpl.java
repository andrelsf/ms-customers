package andrelsf.com.github.msaccounts.services.impl;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostCustomerRequest;
import andrelsf.com.github.msaccounts.api.http.responses.AccountResponse;
import andrelsf.com.github.msaccounts.api.http.responses.CustomerResponse;
import andrelsf.com.github.msaccounts.entities.CustomerEntity;
import andrelsf.com.github.msaccounts.handlers.exceptions.CustomerNotFoundException;
import andrelsf.com.github.msaccounts.repositories.CustomerRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.services.CustomerService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

  private final AccountService accountService;
  private final CustomerRepository customerRepository;

  public CustomerServiceImpl(AccountService accountService, CustomerRepository customerRepository) {
    this.accountService = accountService;
    this.customerRepository = customerRepository;
  }

  @Override
  @Transactional
  public CustomerResponse create(PostCustomerRequest customerRequest) {
    final CustomerEntity customerEntity = Mapper.toCustomerEntity(customerRequest);
    final CustomerEntity customer = customerRepository.save(customerEntity);
    final AccountResponse accountResponse = accountService.create(customer.getId(), customerRequest.account());
    return CustomerResponse.of(customer.getId(), customer.getName(), customer.getCpf(), accountResponse);
  }

  @Override
  public CustomerResponse findById(final String clientId) {
    return customerRepository.findById(clientId)
        .map(Mapper::toCustomerResponse)
        .orElseThrow(() ->
            new CustomerNotFoundException("Customer not found by ID=".concat(clientId)));
  }

  @Override
  public List<CustomerResponse> getAll(Params params) {
    return customerRepository.findAll(params.accountNumber(), params.getPageable())
        .stream()
        .map(Mapper::toCustomerResponse)
        .collect(Collectors.toList());
  }
}
