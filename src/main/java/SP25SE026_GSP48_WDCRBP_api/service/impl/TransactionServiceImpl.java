package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatus;
import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderDeposit;
import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderProgress;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Transaction;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.TransactionUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRes;
import SP25SE026_GSP48_WDCRBP_api.repository.OrderDepositRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.OrderProgressRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.TransactionRepository;
import SP25SE026_GSP48_WDCRBP_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    private OrderDepositRepository orderDepositRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private OrderProgressRepository orderProgressRepository;

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

        if (transaction.getOrderDeposit() != null) {
            OrderDeposit orderDeposit = transaction.getOrderDeposit();
            ServiceOrder serviceOrder = orderDeposit.getServiceOrder();

            orderDeposit.setStatus(true);
            orderDeposit.setUpdatedAt(LocalDateTime.now());
            orderDepositRepository.save(orderDeposit);

            serviceOrder.setAmountPaid(serviceOrder.getAmountPaid() + orderDeposit.getAmount());
            serviceOrder.setAmountRemaining(serviceOrder.getAmountRemaining() - orderDeposit.getAmount());

            OrderProgress newOrderProgress = new OrderProgress();
            newOrderProgress.setServiceOrder(serviceOrder);
            newOrderProgress.setCreatedTime(LocalDateTime.now());

            if (Objects.equals(serviceOrder.getStatus(), ServiceOrderStatus.DA_DUYET_HOP_DONG)) {
                if (serviceOrder.getAvailableService().getService().getServiceName().equals(ServiceNameConstant.CUSTOMIZATION)) {
                    newOrderProgress.setStatus(ServiceOrderStatus.DANG_GIA_CONG);
                    orderProgressRepository.save(newOrderProgress);

                    serviceOrder.setStatus(ServiceOrderStatus.DANG_GIA_CONG);
                    serviceOrder.setFeedback("");
                    serviceOrder.setRole("Woodworker");
                    serviceOrderRepository.save(serviceOrder);
                } else if (serviceOrder.getAvailableService().getService().getServiceName().equals(ServiceNameConstant.PERSONALIZATION)) {
                    serviceOrder.setFeedback("");
                    serviceOrder.setRole("Woodworker");
                    serviceOrderRepository.save(serviceOrder);
                }
            } else if (Objects.equals(serviceOrder.getStatus(), ServiceOrderStatus.DA_DUYET_THIET_KE)) {

            } else if (Objects.equals(serviceOrder.getStatus(), ServiceOrderStatus.DANG_GIAO_HANG_LAP_DAT)) {

            }
        }
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
