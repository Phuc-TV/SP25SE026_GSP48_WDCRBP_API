package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.config.VnPayConfig;
import SP25SE026_GSP48_WDCRBP_api.constant.TransactionTypeConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServicePack;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Transaction;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentOrderRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentServicePackRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentWalletRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.PaymentRes;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.VNPayService;
import SP25SE026_GSP48_WDCRBP_api.util.AESUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayServiceImp implements VNPayService {

    private static final String PAYMENT_URL = "http://localhost:5173/payment-success";
    private static final String AES_KEY = "YourSecretKey123";
    private final UserRepository userRepository;
    private final OrderDepositRepository orderDepositRepository;
    private final TransactionRepository transactionRepository;
    private final ServicePackRepository servicePackRepository;
    private final WalletRepository walletRepository;
    private final MailServiceImpl mailServiceImpl;
    private final WoodworkerProfileRepository woodworkerProfileRepository;

    @Override
    public PaymentRes processOrderPayment(PaymentOrderRequest request) {
        long amount = request.getAmount();
        String email = request.getEmail();
        String userId = request.getUserId();
        String orderDepositId = request.getOrderDepositId();
        String transactionType = request.getTransactionType();
        if (transactionType == null || transactionType.isBlank()) {
            transactionType = "pay_online";
        }

        try {
            Long parsedUserId = Long.parseLong(userId);
            Long parsedOrderDepositId = Long.parseLong(orderDepositId);

            User dbUser = userRepository.findById(parsedUserId)
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "không tìm thấy mã người dùng: " + userId));

            if (!"Customer".equalsIgnoreCase(dbUser.getRole())) {
                throw new WDCRBPApiException(HttpStatus.FORBIDDEN, "chỉ khách hàng mới được sài dịch vụ này..");
            }

            var orderDeposit = orderDepositRepository.findById(parsedOrderDepositId)
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "không tìm thấy mã cọc đơn hàng: " + orderDepositId));


            Transaction txn = Transaction.builder()
                    .transactionType(transactionType)
                    .amount(amount)
                    .description("Thanh toán cọc đơn hàng")
                    .status(false)
                    .createdAt(LocalDateTime.now())
                    .user(dbUser)
                    .orderDeposit(orderDeposit)
                    .build();
            transactionRepository.save(txn);
            // Create a payment URL
            String encryptedTransactionId = AESUtil.encrypt(String.valueOf(txn.getTransactionId()), AES_KEY);
            Map<String, String> vnp_Params = new HashMap<>();
            String returnUrl = PAYMENT_URL + "?" + "transactionId=" + URLEncoder.encode(encryptedTransactionId, StandardCharsets.UTF_8);
            long vnpAmount = amount * 100 ;
            String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            String vnp_IpAddr = "127.0.0.1";
            vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            Calendar cal = Calendar.getInstance(tz);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(tz);
            String vnp_CreateDate = sdf.format(cal.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            cal.add(Calendar.MINUTE, 30);
            String vnp_ExpireDate = sdf.format(cal.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashData.append('&');
                    query.append('&');
                }
            }
            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + query;
            mailServiceImpl.sendEmail(email, "VNPay Payment Link", "payment", paymentUrl);
            return PaymentRes.builder()
                    .status("ok")
                    .message("Payment URL đã được tạo thành công và gửi đến email của bạn.")
                    .URL(paymentUrl)
                    .expirationDate(vnp_ExpireDate.substring(0, 8))
                    .expirationTime(vnp_ExpireDate.substring(8))
                    .timeZone(tz.getID())
                    .build();

        } catch (NumberFormatException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Không đúng định dạng mã: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Không xử lý được thanh toán VNPay: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentRes processServicePackPayment(PaymentServicePackRequest request) {
        Long servicePackId = Long.parseLong(request.getServicePackId());

        // Fetch the ServicePack (assuming the ServicePack is an entity already in your database)
        ServicePack servicePack = servicePackRepository.findById(servicePackId)
                .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy gói dịch vụ."));

        long amount = (long)servicePack.getPrice().floatValue();
        String email = request.getEmail();
        String userId = request.getUserId();

        try {
            Long parsedUserId = Long.parseLong(userId);
            Long parsedServicePackId = Long.parseLong(request.getServicePackId());

            User dbUser = userRepository.findById(parsedUserId)
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "không tìm thấy mã người dùng: " + userId));

            if (!"Woodworker".equalsIgnoreCase(dbUser.getRole())) {
                throw new WDCRBPApiException(HttpStatus.FORBIDDEN, "Chỉ có thợ mộc mới được phép mua gói dịch vụ.");
            }

            // use userId to get the woodworkerId
            WoodworkerProfile woodworkerProfile = woodworkerProfileRepository.findByUser_UserId(dbUser.getUserId())
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ thợ mộc cho người dùng: " + userId));

            Long wwId = woodworkerProfile.getWoodworkerId();

            Transaction txn = Transaction.builder()
                    .transactionType(TransactionTypeConstant.THANH_TOAN_QUA_CONG)
                    .amount(amount)
                    .description("Thanh toán gói dịch vụ: " + servicePack.getName())
                    .status(false)
                    .createdAt(LocalDateTime.now())
                    .user(dbUser)
                    .build();
            transactionRepository.save(txn);

            String encryptedWoodworkerId = AESUtil.encrypt(String.valueOf(wwId), AES_KEY);
            String encryptedServicePackId = AESUtil.encrypt(String.valueOf(parsedServicePackId), AES_KEY);
            String encryptedTransactionId = AESUtil.encrypt(String.valueOf(txn.getTransactionId()), AES_KEY);

            Map<String, String> vnp_Params = new HashMap<>();

            String returnUrl = PAYMENT_URL + "?" +
                    "WoodworkerId=" + URLEncoder.encode(encryptedWoodworkerId, StandardCharsets.UTF_8) +
                    "&ServicePackId=" + URLEncoder.encode(encryptedServicePackId, StandardCharsets.UTF_8) +
                    "&TransactionId=" + URLEncoder.encode(encryptedTransactionId, StandardCharsets.UTF_8);
            vnp_Params.put("vnp_ReturnUrl", returnUrl);

            // VNPay URL creation
            long vnpAmount = amount * 100 ;
            String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            String vnp_IpAddr = "127.0.0.1";

            vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_OrderType", "other");

            TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            Calendar cal = Calendar.getInstance(tz);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(tz);

            String vnp_CreateDate = sdf.format(cal.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cal.add(Calendar.MINUTE, 30);
            String vnp_ExpireDate = sdf.format(cal.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashData.append('&');
                    query.append('&');
                }
            }

            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + query;

            mailServiceImpl.sendEmail(email, "VNPay Payment Link", "payment", paymentUrl);

            return PaymentRes.builder()
                    .status("ok")
                    .message("Payment URL đã được tạo thành công và gửi đến email của bạn.")
                    .URL(paymentUrl)
                    .expirationDate(vnp_ExpireDate.substring(0, 8))
                    .expirationTime(vnp_ExpireDate.substring(8))
                    .timeZone(tz.getID())
                    .build();

        } catch (NumberFormatException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Không đúng định dạng mã : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Không xử lý được thanh toán VNPay: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentRes processWalletPayment(PaymentWalletRequest request) {
        long amount = request.getAmount();
        String email = request.getEmail();
        String userId = request.getUserId();
        String walletId = request.getWalletId();
        String transactionType = request.getTransactionType();
        if (transactionType == null || transactionType.isBlank()) {
            transactionType = "pay_online";
        }

        try {
            Long parsedUserId = Long.parseLong(userId);
            Long parsedWalletId = Long.parseLong(walletId);

            User dbUser = userRepository.findById(parsedUserId)
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

            if (!"Customer".equalsIgnoreCase(dbUser.getRole()) && !"Woodworker".equalsIgnoreCase(dbUser.getRole())) {
                throw new WDCRBPApiException(HttpStatus.FORBIDDEN, "chỉ có thợ mộc và khách hàng mới được sài dịch vụ này.");
            }

            var wallet = walletRepository.findById(parsedWalletId)
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "Wallet not found for ID: " + walletId));


            Transaction txn = Transaction.builder()
                    .transactionType(transactionType)
                    .amount(amount)
                    .description("Nạp tiền vào ví")
                    .status(false)
                    .createdAt(LocalDateTime.now())
                    .user(dbUser)
                    .wallet(wallet)
                    .build();
            transactionRepository.save(txn);

            String encryptedWalletId = AESUtil.encrypt(String.valueOf(txn.getWallet().getWalletId()), AES_KEY);
            String encryptedTransactionId = AESUtil.encrypt(String.valueOf(txn.getTransactionId()), AES_KEY);
            Map<String, String> vnp_Params = new HashMap<>();
            String returnUrl = PAYMENT_URL + "?" +
                    "WalletId=" + URLEncoder.encode(encryptedWalletId, StandardCharsets.UTF_8) +
                    "&TransactionId=" + URLEncoder.encode(encryptedTransactionId, StandardCharsets.UTF_8);
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            long vnpAmount = amount * 100 ;
            String vnp_TxnRef = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
            String vnp_IpAddr = "127.0.0.1";
            vnp_Params.put("vnp_Version", VnPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_ReturnUrl", returnUrl);

            TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
            Calendar cal = Calendar.getInstance(tz);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            sdf.setTimeZone(tz);

            String vnp_CreateDate = sdf.format(cal.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cal.add(Calendar.MINUTE, 30);
            String vnp_ExpireDate = sdf.format(cal.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashData.append('&');
                    query.append('&');
                }
            }

            String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + query;

            mailServiceImpl.sendEmail(email, "VNPay Payment Link", "payment", paymentUrl);

            return PaymentRes.builder()
                    .status("ok")
                    .message("Payment URL đã được tạo thành công và gửi đến email của bạn.")
                    .URL(paymentUrl)
                    .expirationDate(vnp_ExpireDate.substring(0, 8))
                    .expirationTime(vnp_ExpireDate.substring(8))
                    .timeZone(tz.getID())
                    .build();

        } catch (NumberFormatException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Không đúng định dạng mã : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Không xử lý được thanh toán VNPay: " + e.getMessage(), e);
        }
    }

}

