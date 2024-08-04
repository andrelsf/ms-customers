package andrelsf.com.github.msaccounts.api.http.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PostTransferRequest(
    @NotNull @Positive Integer agency,
    @NotNull @Positive Integer accountNumber,
    @NotNull BigDecimal amount
) {}
