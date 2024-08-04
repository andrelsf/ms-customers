package andrelsf.com.github.msaccounts.services;

import andrelsf.com.github.msaccounts.api.http.requests.PostTransferRequest;
import andrelsf.com.github.msaccounts.api.http.responses.TransferResponse;
import andrelsf.com.github.msaccounts.entities.TransferRecord;
import java.util.List;
import java.util.UUID;

public interface TransferService {
  List<TransferResponse> getAllTransfers(final UUID accountId);
  TransferResponse registerTransfer(final TransferRecord transferRecord);
  UUID doTransfer(final UUID accountId, final PostTransferRequest request);
}
