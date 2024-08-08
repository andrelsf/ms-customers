package andrelsf.com.github.msaccounts.services.impl;

import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostCustomerRequest;
import andrelsf.com.github.msaccounts.api.http.responses.CustomerResponse;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import andrelsf.com.github.msaccounts.entities.CustomerEntity;
import andrelsf.com.github.msaccounts.handlers.exceptions.CustomerNotFoundException;
import andrelsf.com.github.msaccounts.repositories.CustomerRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.services.CustomerService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
  public String create(PostCustomerRequest customerRequest) {
    AtomicReference<String> customerAtomicReference = new AtomicReference<>();
    customerRepository.findByCpf(customerRequest.cpf())
        .ifPresentOrElse(customer -> {
          customerAtomicReference.set(customer.getId());
        }, () -> {
          final CustomerEntity customerEntity = Mapper.toCustomerEntity(customerRequest);
          final CustomerEntity customer = customerRepository.save(customerEntity);
          accountService.create(customer.getId(), customerRequest.account());
          customerAtomicReference.set(customer.getId());
        });
    return customerAtomicReference.get();
  }

  private CustomerEntity find(final String customerId, final AccountStatus status) {
    return customerRepository.findByIdAndAccount_Status(customerId, status)
        .orElseThrow(() ->
            new CustomerNotFoundException("Customer not found by ID=".concat(customerId)));
  }

  @Override
  @Transactional
  public void activateCustomer(UUID customerId) {
    CustomerEntity customer = this.find(customerId.toString(), AccountStatus.INACTIVE);
    customer.activate();
    customerRepository.save(customer);
  }

  @Override
  @Transactional
  public void inactivateCustomer(UUID customerId) {
    CustomerEntity customer = this.find(customerId.toString(), AccountStatus.ACTIVE);
    customer.inactivate();
    customerRepository.save(customer);
  }

  @Override
  public CustomerResponse findById(final String customerId) {
    final CustomerEntity customer = this.find(customerId, AccountStatus.ACTIVE);
    return Mapper.toCustomerResponse(customer);
  }

  @Override
  public List<CustomerResponse> getAll(Params params) {
    return customerRepository.findAll(
        params.status(), params.accountNumber(), params.getPageable())
        .stream()
        .map(Mapper::toCustomerResponse)
        .collect(Collectors.toList());
  }
}
