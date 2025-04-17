package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.GuaranteeOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.TransactionTypeConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.TransactionUpdateRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
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
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private OrderProgressRepository orderProgressRepository;
    @Autowired
    private GuaranteeOrderRepository guaranteeOrderRepository;

    private void updateGuaranteeOrderProgress(Transaction transaction) {
        if (transaction.getOrderDeposit() != null) {
            OrderDeposit orderDeposit = transaction.getOrderDeposit();
            GuaranteeOrder guaranteeOrder = orderDeposit.getGuaranteeOrder();

            orderDeposit.setStatus(true);
            orderDeposit.setUpdatedAt(LocalDateTime.now());
            orderDepositRepository.save(orderDeposit);

            guaranteeOrder.setAmountPaid(guaranteeOrder.getAmountPaid() + orderDeposit.getAmount());
            guaranteeOrder.setAmountRemaining(guaranteeOrder.getAmountRemaining() - orderDeposit.getAmount());

            OrderProgress newOrderProgress = new OrderProgress();
            newOrderProgress.setGuaranteeOrder(guaranteeOrder);
            newOrderProgress.setCreatedTime(LocalDateTime.now());

            if (Objects.equals(guaranteeOrder.getStatus(), GuaranteeOrderStatusConstant.DA_DUYET_BAO_GIA)) {
                newOrderProgress.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_NHAN_HANG);
                orderProgressRepository.save(newOrderProgress);

                guaranteeOrder.setStatus(GuaranteeOrderStatusConstant.DANG_CHO_NHAN_HANG);
                guaranteeOrder.setFeedback("");
                guaranteeOrder.setRole("Woodworker");
                guaranteeOrderRepository.save(guaranteeOrder);
            } else if (Objects.equals(guaranteeOrder.getStatus(), GuaranteeOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT)) {
                newOrderProgress.setStatus(ServiceOrderStatusConstant.DA_HOAN_TAT);
                orderProgressRepository.save(newOrderProgress);

                guaranteeOrder.setStatus(ServiceOrderStatusConstant.DA_HOAN_TAT);
                guaranteeOrder.setFeedback("");
                guaranteeOrder.setRole("");
                guaranteeOrder.setUpdatedAt(LocalDateTime.now());
                guaranteeOrderRepository.save(guaranteeOrder);

                addMoneyToWWWalletForGuaranteeOrder(guaranteeOrder.getAvailableService().getWoodworkerProfile().getUser().getUserId(), guaranteeOrder);
            }
        }
    }

    private void updateServiceOrderProgress(Transaction transaction) {
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

            if (Objects.equals(serviceOrder.getStatus(), ServiceOrderStatusConstant.DA_DUYET_HOP_DONG)) {
                if (serviceOrder.getAvailableService().getService().getServiceName().equals(ServiceNameConstant.CUSTOMIZATION)) {
                    newOrderProgress.setStatus(ServiceOrderStatusConstant.DANG_GIA_CONG);
                    orderProgressRepository.save(newOrderProgress);

                    serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_GIA_CONG);
                    serviceOrder.setFeedback("");
                    serviceOrder.setRole("Woodworker");
                    serviceOrderRepository.save(serviceOrder);
                } else if (serviceOrder.getAvailableService().getService().getServiceName().equals(ServiceNameConstant.PERSONALIZATION)) {
                    serviceOrder.setFeedback("");
                    serviceOrder.setRole("Woodworker");
                    serviceOrderRepository.save(serviceOrder);
                }
            } else if (Objects.equals(serviceOrder.getStatus(), ServiceOrderStatusConstant.DA_DUYET_THIET_KE)) {
                newOrderProgress.setStatus(ServiceOrderStatusConstant.DANG_GIA_CONG);
                orderProgressRepository.save(newOrderProgress);

                serviceOrder.setStatus(ServiceOrderStatusConstant.DANG_GIA_CONG);
                serviceOrder.setFeedback("");
                serviceOrder.setRole("Woodworker");
                serviceOrderRepository.save(serviceOrder);
            } else if (Objects.equals(serviceOrder.getStatus(), ServiceOrderStatusConstant.DANG_GIAO_HANG_LAP_DAT)) {
                newOrderProgress.setStatus(ServiceOrderStatusConstant.DA_HOAN_TAT);
                orderProgressRepository.save(newOrderProgress);

                serviceOrder.setStatus(ServiceOrderStatusConstant.DA_HOAN_TAT);
                serviceOrder.setFeedback("");
                serviceOrder.setRole("");
                serviceOrder.setUpdatedAt(LocalDateTime.now());
                serviceOrderRepository.save(serviceOrder);

                addMoneyToWWWalletForServiceOrder(serviceOrder.getAvailableService().getWoodworkerProfile().getUser().getUserId(), serviceOrder);
            }
        }
    }

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

        if (transaction.getOrderDeposit() != null && transaction.getOrderDeposit().getServiceOrder() != null) {
            updateServiceOrderProgress(transaction);
        } else if (transaction.getOrderDeposit() != null && transaction.getOrderDeposit().getGuaranteeOrder() != null) {
            updateGuaranteeOrderProgress(transaction);
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

    @Override
    public void addMoneyToWWWalletForServiceOrder(Long userId, ServiceOrder serviceOrder) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
        Wallet wallet = walletRepository.findByUser_UserId(userId).orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Wallet not found with ID: " + userId));

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionTypeConstant.NHAN_TIEN);
        transaction.setDescription("Nhận tiền hoàn thành đơn hàng " + serviceOrder.getOrderId());
        transaction.setAmount(serviceOrder.getTotalAmount());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(true);
        transaction.setUser(user);
        transaction.setOrderDeposit(null);
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);

        wallet.setBalance(wallet.getBalance() + serviceOrder.getTotalAmount());
        walletRepository.save(wallet);
    }

    @Override
    public void addMoneyToWWWalletForGuaranteeOrder(Long userId, GuaranteeOrder guaranteeOrder) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
        Wallet wallet = walletRepository.findByUser_UserId(userId).orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Wallet not found with ID: " + userId));

        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionTypeConstant.NHAN_TIEN);
        transaction.setDescription("Nhận tiền hoàn thành đơn hàng sửa chữa / bảo hành " + guaranteeOrder.getGuaranteeOrderId());
        transaction.setAmount(guaranteeOrder.getTotalAmount());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(true);
        transaction.setUser(user);
        transaction.setOrderDeposit(null);
        transaction.setWallet(wallet);
        transactionRepository.save(transaction);

        wallet.setBalance(wallet.getBalance() + guaranteeOrder.getTotalAmount());
        walletRepository.save(wallet);
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
