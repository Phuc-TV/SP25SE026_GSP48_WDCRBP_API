package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.GuaranteeOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceNameConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.ServiceOrderStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.TransactionTypeConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.WalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ListTransactionRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WalletRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WoodworkerProfileServiceImpl woodworkerProfileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailServiceImpl mailServiceImpl;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ServicePackRepository servicePackRepository;

    @Autowired
    private OrderDepositRepository orderDepositRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    @Autowired
    private OrderProgressRepository orderProgressRepository;

    @Autowired
    private WoodworkerProfileRepository woodworkerProfileRepository;
    @Autowired
    private GuaranteeOrderRepository guaranteeOrderRepository;

    @Override
    public WalletRes getWalletByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + userId));

        Wallet wallet = walletRepository.findAll().stream()
                .filter(w -> w.getUser() != null && w.getUser().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy ví cho người dùng ID: " + userId));

        if (user == null || wallet == null) {
            throw new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng hoặc ví.");
        }

        WalletRes rest = new WalletRes();
        rest.setWalletId(wallet.getWalletId());
        rest.setBalance(wallet.getBalance());
        rest.setCreatedAt(wallet.getCreatedAt());
        rest.setUpdatedAt(wallet.getUpdatedAt());
        rest.setUserId(userId);

        return rest;
    }

    @Override
    public WalletRes updateBalanceWallet(WalletRequest walletRequest) {
        // Fetch wallet by walletId
        Wallet wallet = walletRepository.findById(walletRequest.getWalletId())
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy ví với ID: " + walletRequest.getWalletId()));

        // Update wallet balance by adding the amount
        float newBalance = wallet.getBalance() + walletRequest.getAmount();

        // If balance goes negative, throw an error
        if (newBalance < 0) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Không thể có số dư âm trong ví.");
        }

        wallet.setBalance(newBalance);
        wallet.setUpdatedAt(LocalDateTime.now()); // Set the updated timestamp

        // Save the updated wallet object
        walletRepository.save(wallet);

        // Prepare and return the response DTO
        WalletRes walletRes = new WalletRes();
        walletRes.setWalletId(wallet.getWalletId());
        walletRes.setBalance(wallet.getBalance());
        walletRes.setCreatedAt(wallet.getCreatedAt());
        walletRes.setUpdatedAt(wallet.getUpdatedAt());
        walletRes.setUserId(wallet.getUser().getUserId());

        return walletRes;
    }

    private void updateServiceOrderProgress(OrderDeposit orderDeposit) {
        ServiceOrder serviceOrder = orderDeposit.getServiceOrder();
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
            serviceOrder.setUpdatedAt(LocalDateTime.now());
            serviceOrder.setFeedback("");
            serviceOrder.setRole("");
            serviceOrderRepository.save(serviceOrder);

            transactionService.addMoneyToWWWalletForServiceOrder(serviceOrder.getAvailableService().getWoodworkerProfile().getUser().getUserId(), serviceOrder);
        }
    }

    private void updateGuaranteeOrderProgress(OrderDeposit orderDeposit) {
        GuaranteeOrder guaranteeOrder = orderDeposit.getGuaranteeOrder();
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

            transactionService.addMoneyToWWWalletForGuaranteeOrder(guaranteeOrder.getAvailableService().getWoodworkerProfile().getUser().getUserId(), guaranteeOrder);
        }
    }

    @Override
    public ListTransactionRes createWalletOrderPayment(PaymentOrderRequest request) {
        Long userId = Long.parseLong(request.getUserId());
        Long orderDepositId = Long.parseLong(request.getOrderDepositId());
        String email = request.getEmail();

        OrderDeposit orderDeposit = orderDepositRepository.findById(orderDepositId).get();

        User dbUser = userRepository.findById(userId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + userId));
        if (!"Customer".equalsIgnoreCase(dbUser.getRole())) {
            throw new WDCRBPApiException(HttpStatus.FORBIDDEN, "Chỉ khách hàng mới được phép mua sản phẩm.");
        }
        Wallet wallet = walletRepository.findAll().stream()
                .filter(w -> w.getUser() != null && w.getUser().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy ví cho người dùng ID: " + userId));
        if (wallet.getBalance() < orderDeposit.getAmount()) {
            // Not enough balance, send failure email
            String failureMessage = "Thanh toán không thành công do số dư không đủ.";
            mailServiceImpl.sendEmail(email, "Lỗi thanh toán", "payment", failureMessage);
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Số dư không đủ.");
        }
        wallet.setBalance(wallet.getBalance() - orderDeposit.getAmount());
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        orderDeposit.setStatus(true);
        orderDeposit.setUpdatedAt(LocalDateTime.now());
        orderDepositRepository.save(orderDeposit);

        Transaction txn = new Transaction();
        txn.setTransactionType(TransactionTypeConstant.THANH_TOAN_BANG_VI);
        txn.setAmount(orderDeposit.getAmount());
        txn.setDescription("Thanh toán đặt cọc đơn hàng");
        txn.setCreatedAt(LocalDateTime.now());
        txn.setStatus(true);
        txn.setUser(dbUser);
        txn.setOrderDeposit(orderDeposit);
        txn.setWallet(wallet);
        transactionRepository.save(txn);

        String successMessage = "Thanh toán của bạn đã thành công!";
        mailServiceImpl.sendEmail(email, "Thanh toán thành công", "payment", successMessage);
        ListTransactionRes.Data transactionData = new ListTransactionRes.Data();
        transactionData.setTransactionId(txn.getTransactionId());
        transactionData.setTransactionType(txn.getTransactionType());
        transactionData.setAmount(txn.getAmount());
        transactionData.setDescription(txn.getDescription());
        transactionData.setCreatedAt(txn.getCreatedAt());
        transactionData.setStatus(txn.isStatus());
        transactionData.setUserId(txn.getUser().getUserId());
        transactionData.setOrderDepositId(txn.getOrderDeposit() != null ? txn.getOrderDeposit().getOrderDepositId() : null);
        transactionData.setWalletId(txn.getWallet().getWalletId());
        ListTransactionRes response = new ListTransactionRes();
        response.setData(Collections.singletonList(transactionData));

        if (orderDeposit.getServiceOrder() != null) {
            updateServiceOrderProgress(orderDeposit);
        } else if (orderDeposit.getGuaranteeOrder() != null) {
            updateGuaranteeOrderProgress(orderDeposit);
        }

        return response;
    }

    @Override
    public ListTransactionRes createWalletServicePackPayment(PaymentServicePackRequest request) {
        Long userId = Long.parseLong(request.getUserId());
        Long servicePackId = Long.parseLong(request.getServicePackId());
        String email = request.getEmail();
        // Fetch the ServicePack (assuming the ServicePack is an entity already in your database)
        ServicePack servicePack = servicePackRepository.findById(servicePackId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy gói dịch vụ."));
        Float amount = servicePack.getPrice();

        // Step 1: Find the user and verify it's a Woodworker
        User dbUser = userRepository.findById(userId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + userId));

        if (!"Woodworker".equalsIgnoreCase(dbUser.getRole())) {
            throw new WDCRBPApiException(HttpStatus.FORBIDDEN, "Chỉ thợ mộc mới được phép mua gói dịch vụ.");
        }

        // Step 2: Find the wallet of the user
        Wallet wallet = walletRepository.findAll().stream()
                .filter(w -> w.getUser() != null && w.getUser().getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy ví cho người dùng ID: " + userId));

        // Step 3: Check if wallet balance is sufficient
        if (wallet.getBalance() < amount) {
            // Not enough balance, send failure email
            String failureMessage = "Thanh toán không thành công do số dư không đủ.";
            mailServiceImpl.sendEmail(email, "Lỗi thanh toán", "payment", failureMessage);
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Số dư không đủ.");
        }

        // Step 4: Deduct the balance from the wallet
        wallet.setBalance(wallet.getBalance() - amount);
        wallet.setUpdatedAt(LocalDateTime.now());
        walletRepository.save(wallet);

        // Step 5: Create a new transaction
        Transaction txn = new Transaction();
        txn.setTransactionType(TransactionTypeConstant.THANH_TOAN_BANG_VI);
        txn.setAmount(amount);
        txn.setDescription("Thanh toán gói dịch vụ " + servicePack.getName());
        txn.setCreatedAt(LocalDateTime.now());
        txn.setStatus(true);
        txn.setUser(dbUser);
        txn.setWallet(wallet);
        transactionRepository.save(txn);

        // Step 6: Retrieve the WoodworkerProfile from the WoodworkerProfileRepository and update service pack information
        WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ thợ mộc cho người dùng ID: " + userId));

        // Update WoodworkerProfile with ServicePack details
        woodworkerProfileService.addServicePack(servicePack.getServicePackId(), woodworkerProfile.getWoodworkerId());

        // Save the updated WoodworkerProfile
        woodworkerProfileRepository.save(woodworkerProfile);

        // Step 7: Send success email to user
        mailServiceImpl.sendEmail(email, "[WDCRBP] Kích hoạt gói dịch vụ thành công", "buy-pack-success", servicePack.getDescription());

        // Step 8: Build and return the response DTO
        ListTransactionRes.Data transactionData = new ListTransactionRes.Data();
        transactionData.setTransactionId(txn.getTransactionId());
        transactionData.setTransactionType(txn.getTransactionType());
        transactionData.setAmount(txn.getAmount());
        transactionData.setDescription(txn.getDescription());
        transactionData.setCreatedAt(txn.getCreatedAt());
        transactionData.setStatus(txn.isStatus());
        transactionData.setUserId(txn.getUser().getUserId());
        transactionData.setOrderDepositId(txn.getOrderDeposit() != null ? txn.getOrderDeposit().getOrderDepositId() : null);
        transactionData.setWalletId(txn.getWallet().getWalletId());

        ListTransactionRes response = new ListTransactionRes();
        response.setData(Collections.singletonList(transactionData));

        return response;
    }

}
