package andrelsf.com.github.msaccounts.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import andrelsf.com.github.msaccounts.api.http.requests.AccountRequest;
import andrelsf.com.github.msaccounts.api.http.requests.Params;
import andrelsf.com.github.msaccounts.api.http.requests.PostCustomerRequest;
import andrelsf.com.github.msaccounts.api.http.responses.CustomerResponse;
import andrelsf.com.github.msaccounts.entities.AccountEntity;
import andrelsf.com.github.msaccounts.entities.AccountStatus;
import andrelsf.com.github.msaccounts.entities.CustomerEntity;
import andrelsf.com.github.msaccounts.handlers.exceptions.CustomerNotFoundException;
import andrelsf.com.github.msaccounts.repositories.CustomerRepository;
import andrelsf.com.github.msaccounts.services.AccountService;
import andrelsf.com.github.msaccounts.utils.Mapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

  @Mock
  private AccountService accountService;

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerServiceImpl customerService;

  @Test
  @DisplayName("Dado o PostClientRequest valido deve salvar o cliente d retornar o ID")
  void test_create() {
    final String clientIdExpected = UUID.randomUUID().toString();
    final PostCustomerRequest postClientRequest = new PostCustomerRequest(
        "Jose Nome Facil", "11122233344", new AccountRequest(1234, 4321));
    final CustomerEntity clientEntity = Mapper.toCustomerEntity(postClientRequest);
    CustomerEntity customerEntityReturned = Mapper.toCustomerEntity(postClientRequest);
    customerEntityReturned.setId(clientIdExpected);

    when(customerRepository.save(any(CustomerEntity.class)))
        .thenReturn(customerEntityReturned);

    final String customerId = customerService.create(postClientRequest);

    assertThat(customerId)
        .isNotBlank()
        .isEqualTo(clientIdExpected);
  }

  @Test
  @DisplayName("Dado o customerId valido deve consultar e retornar um cliente")
  void test_findById() {
    final String customerId = UUID.randomUUID().toString();
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.fillWith(customerId, 1234, 4321);
    final CustomerEntity customerEntity = new CustomerEntity(
        customerId, "Jose Nome Facil", "11122233344", accountEntity);

    when(customerRepository.findByIdAndAccount_Status(customerId, AccountStatus.ACTIVE))
        .thenReturn(Optional.of(customerEntity));

    final CustomerResponse customerResponse = customerService.findById(customerId);

    assertThat(customerResponse)
        .isNotNull()
        .isInstanceOf(CustomerResponse.class);

    assertThat(customerResponse.customerId())
        .isNotBlank()
        .isEqualTo(customerId);
  }

  @Test
  @DisplayName("Dado customerId invalido deve lancar exception CustomerNotFoundException")
  void test_findById_notFound() {
    final String customerIdInvalid = UUID.randomUUID().toString();

    when(customerRepository.findByIdAndAccount_Status(customerIdInvalid, AccountStatus.ACTIVE))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        customerService.findById(customerIdInvalid))
        .isInstanceOf(CustomerNotFoundException.class)
        .hasMessage("Customer not found by ID=".concat(customerIdInvalid));
  }

  @Test
  @DisplayName("Deve retorna uma lista de clientes")
  void test_getAll() {
    final String customerId = UUID.randomUUID().toString();
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.fillWith(customerId, 1234, 4321);
    final CustomerEntity clientEntity = new CustomerEntity(
        customerId, "Jose Nome Facil", "11122233344", accountEntity);
    final List<CustomerEntity> customers = List.of(clientEntity);
    final Params params = Params.of("ACTIVE", null, 0, 10);

    when(customerRepository.findAll(params.status(), params.accountNumber(), params.getPageable()))
        .thenReturn(customers);

    final List<CustomerResponse> clientsResponse = customerService.getAll(params);

    assertThat(clientsResponse)
        .isNotEmpty()
        .hasSize(1);
  }

  @Test
  @DisplayName("Deve inativar o cliente pelo seu identificador")
  void test_inactivateCustomer() {
    final UUID customerId = UUID.randomUUID();
    AccountEntity accountEntity = new AccountEntity();
    accountEntity.fillWith(customerId.toString(), 1234, 4321);
    final CustomerEntity customerEntity = new CustomerEntity(
        customerId.toString(), "Jose Nome Facil", "11122233344", accountEntity);

    when(customerRepository.findByIdAndAccount_Status(customerId.toString(), AccountStatus.ACTIVE))
        .thenReturn(Optional.of(customerEntity));

    customerService.inactivateCustomer(customerId);

    verify(customerRepository, times(1)).save(customerEntity);
  }
}
