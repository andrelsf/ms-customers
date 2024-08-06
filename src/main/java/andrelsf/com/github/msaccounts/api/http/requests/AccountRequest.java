package andrelsf.com.github.msaccounts.api.http.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AccountRequest(
    @NotNull @Positive Integer agency,
    @NotNull @Positive Integer accountNumber
) {

}
