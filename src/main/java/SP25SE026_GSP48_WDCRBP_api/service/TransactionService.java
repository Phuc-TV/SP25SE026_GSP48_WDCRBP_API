package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.TransactionUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRest;

import java.util.List;

public interface TransactionService {
    void updateTransaction(TransactionUpdateRequest request);
    List<ListTransactionRest.Data> getAllTransactions();
    List<ListTransactionRest.Data> getTransactionsByStatus(boolean status);
    List<ListTransactionRest.Data> getTransactionById(Long transactionId);
    List<ListTransactionRest.Data> getTransactionsByUserId(Long userId);
}
