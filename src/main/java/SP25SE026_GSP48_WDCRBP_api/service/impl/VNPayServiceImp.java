package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.config.VnPayConfig;
import SP25SE026_GSP48_WDCRBP_api.model.entity.PaymentMethod;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Transaction;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.exception.WDCRBPApiException;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.PaymentRest;
import SP25SE026_GSP48_WDCRBP_api.repository.OrderDepositRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.PaymentMethodRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.TransactionRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.service.MailService;
import SP25SE026_GSP48_WDCRBP_api.service.VNPayService;
import com.google.api.client.util.DateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayServiceImp implements VNPayService {

    private final UserRepository userRepository;
    private final OrderDepositRepository orderDepositRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransactionRepository transactionRepository;
    private final MailService mailService;

    private static final long MAX_AMOUNT = 1_000_000_000L;

    @Override
    public PaymentRest processPayment(PaymentRequest request) {
        long amount = request.getAmount();
        String email = request.getEmail();
        String userId = request.getUserId();
        String orderDepositId = request.getOrderDepositId();

        if (amount > MAX_AMOUNT) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Amount exceeds the allowed limit.");
        }

        try {
            Long parsedUserId = Long.parseLong(userId);
            Long parsedOrderDepositId = Long.parseLong(orderDepositId );

            User dbUser = userRepository.findById(parsedUserId)
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

            var orderDeposit = orderDepositRepository.findById(parsedOrderDepositId)
                    .orElseThrow(() -> new WDCRBPApiException(HttpStatus.NOT_FOUND, "OrderDeposit not found for ID: " + orderDepositId));

            // VNPay URL creation
            long vnpAmount = amount * 100;
            String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";
            String returnUrl = "http://localhost:5173/payment-successfull";

            Map<String, String> vnp_Params = new HashMap<>();
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

            // Save or reuse payment method
            PaymentMethod paymentMethod = paymentMethodRepository.findByUserAndProviderName(dbUser, "VNPay")
                    .orElseGet(() -> paymentMethodRepository.save(
                            PaymentMethod.builder()
                                    .user(dbUser)
                                    .providerName("VNPay")
                                    .methodType("online")
                                    .accountNumber("VNPay")
                                    .isDefault(false)
                                    .createdAt(LocalDateTime.now())
                                    .build()
                    ));

            // Save transaction
            Transaction txn = Transaction.builder()
                    .transactionType(vnp_TxnRef)
                    .amount(amount)
                    .status(false)
                    .createdAt(LocalDateTime.now())
                    .user(dbUser)
                    .paymentMethod(paymentMethod)
                    .orderDeposit(orderDeposit)
                    .build();
            transactionRepository.save(txn);

            mailService.sendPaymentLink(email, paymentUrl); // Send the email before return

            return PaymentRest.builder()
                    .status("ok")
                    .message("Payment URL generated and email sent successfully.")
                    .URL(paymentUrl)
                    .expirationDate(vnp_ExpireDate.substring(0, 8))
                    .expirationTime(vnp_ExpireDate.substring(8))
                    .timeZone(tz.getID())
                    .build();

        } catch (NumberFormatException e) {
            throw new WDCRBPApiException(HttpStatus.BAD_REQUEST, "Invalid ID format: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to process VNPay payment: " + e.getMessage(), e);
        }
    }
}
