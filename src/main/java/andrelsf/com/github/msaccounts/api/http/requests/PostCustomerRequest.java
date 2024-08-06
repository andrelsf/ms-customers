package andrelsf.com.github.msaccounts.api.http.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record PostCustomerRequest(
    @NotBlank String name,
    @NotBlank @Length(min = 11, max = 11) String cpf,
    @NotNull @Valid AccountRequest account
) {}
