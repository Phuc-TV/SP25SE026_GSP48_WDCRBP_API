package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Transaction;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.TransactionUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.repository.TransactionRepository;
import SP25SE026_GSP48_WDCRBP_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void updateTransaction(TransactionUpdateRequest request) {
        // Get the transaction from the database using the transactionId
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Transaction not found with ID: " + request.getTransactionId()));

        // Update the transaction status if it's provided
        if (request.getStatus() != null) {
            transaction.setStatus(request.getStatus());
        }

        // Update the canceledAt if provided
        if (request.getCanceledAt() != null) {
            transaction.setCanceledAt(request.getCanceledAt());
        }

        // Save the updated transaction back to the database
        transactionRepository.save(transaction);
    }
}
