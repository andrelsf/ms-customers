package andrelsf.com.github.msaccounts.api.http.responses;

public record CustomerResponse(
    String customerId,
    String name,
    String cpf,
    AccountResponse account
) {

  public static CustomerResponse of(String customerId, String name, String cpf, AccountResponse accountResponse) {
    return new CustomerResponse(customerId, name, cpf, accountResponse);
  }
}
