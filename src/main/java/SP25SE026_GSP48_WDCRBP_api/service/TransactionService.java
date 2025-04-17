package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.GuaranteeOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.TransactionUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRes;

import java.util.List;

public interface TransactionService {
    void updateTransaction(TransactionUpdateRequest request);
    List<ListTransactionRes.Data> getAllTransactions();
    List<ListTransactionRes.Data> getTransactionsByStatus(boolean status);
    List<ListTransactionRes.Data> getTransactionById(Long transactionId);
    List<ListTransactionRes.Data> getTransactionsByUserId(Long userId);
    void addMoneyToWWWalletForServiceOrder(Long userId, ServiceOrder serviceOrder);
    void addMoneyToWWWalletForGuaranteeOrder(Long userId, GuaranteeOrder guaranteeOrder);
}
