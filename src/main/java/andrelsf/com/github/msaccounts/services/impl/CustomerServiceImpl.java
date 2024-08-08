package andrelsf.com.github.msaccounts.services.impl;

import static andrelsf.com.github.msaccounts.handlers.exceptions.CustomerNotFoundException.errorMessage;

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
import org.springframework.dao.DataIntegrityViolationException;
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
    AtomicReference<String> customerIdAtomicReference = new AtomicReference<>();
    customerRepository.findByCpf(customerRequest.cpf())
        .ifPresentOrElse(customer -> {
          throw new DataIntegrityViolationException("CPF already registered");
        }, () -> {
          final CustomerEntity customerEntity = Mapper.toCustomerEntity(customerRequest);
          final CustomerEntity customer = customerRepository.save(customerEntity);
          accountService.create(customer.getId(), customerRequest.account());
          customerIdAtomicReference.set(customer.getId());
        });
    return customerIdAtomicReference.get();
  }

  private CustomerEntity find(final String customerId, final AccountStatus status) {
    return customerRepository.findByIdAndAccount_Status(customerId, status)
        .orElseThrow(() ->
            new CustomerNotFoundException(errorMessage.concat(customerId)));
  }

  private CustomerEntity findByIdOnly(final UUID customerId) {
    return customerRepository.findById(customerId.toString())
        .orElseThrow(() ->
            new CustomerNotFoundException(errorMessage.concat(customerId.toString())));
  }

  @Override
  @Transactional
  public void activateCustomer(final UUID customerId) {
    CustomerEntity customer = findByIdOnly(customerId);
    customer.activate();
    customerRepository.save(customer);
  }

  @Override
  @Transactional
  public void inactivateCustomer(final UUID customerId) {
    CustomerEntity customer = findByIdOnly(customerId);
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
