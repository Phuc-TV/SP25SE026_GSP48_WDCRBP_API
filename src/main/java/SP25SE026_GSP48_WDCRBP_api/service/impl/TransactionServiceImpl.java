package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Transaction;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.TransactionUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRes;
import SP25SE026_GSP48_WDCRBP_api.repository.TransactionRepository;
import SP25SE026_GSP48_WDCRBP_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void updateTransaction(TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Transaction not found with ID: " + request.getTransactionId()));
        if (request.getStatus() != null) {
            transaction.setStatus(request.getStatus());
        }
        if (request.getCanceledAt() != null) {
            transaction.setCanceledAt(request.getCanceledAt());
        }
        transactionRepository.save(transaction);
    }

    @Override
    public List<ListTransactionRes.Data> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListTransactionRes.Data> getTransactionsByStatus(boolean status) {
        return transactionRepository.findAll().stream()
                .filter(tx -> tx.isStatus() == status)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListTransactionRes.Data> getTransactionById(Long transactionId) {
        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Transaction not found with ID: " + transactionId));

        return List.of(mapToDto(tx));
    }


    @Override
    public List<ListTransactionRes.Data> getTransactionsByUserId(Long userId) {
        return transactionRepository.findAll().stream()
                .filter(tx -> tx.getUser() != null && tx.getUser().getUserId().equals(userId))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ListTransactionRes.Data mapToDto(Transaction tx) {
        ListTransactionRes.Data dto = new ListTransactionRes.Data();
        dto.setTransactionId(tx.getTransactionId());
        dto.setTransactionType(tx.getTransactionType());
        dto.setDescription(tx.getDescription());
        dto.setAmount(tx.getAmount());
        dto.setCreatedAt(tx.getCreatedAt());
        dto.setCanceledAt(tx.getCanceledAt());
        dto.setStatus(tx.isStatus());
        dto.setUserId(tx.getUser() != null ? tx.getUser().getUserId() : null);
        dto.setOrderDepositId(tx.getOrderDeposit() != null ? tx.getOrderDeposit().getOrderDepositId() : null);
        dto.setWalletId(tx.getWallet() != null ? tx.getWallet().getWalletId() : null);
        return dto;
    }
}
