package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.constant.ComplainStatusConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.PaymentForConstant;
import SP25SE026_GSP48_WDCRBP_api.constant.TransactionTypeConstant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.ComplaintService;
import SP25SE026_GSP48_WDCRBP_api.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {
    private final ServiceOrderRepository serviceOrderRepository;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ServiceOrderService serviceOrderService;
    private final ModelMapper modelMapper;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public ComplaintRes createComplaint(CreateComplaintRequest request) {
        ServiceOrder order = serviceOrderRepository.findById(request.getServiceOrderId())
                .orElseThrow(() -> new RuntimeException("ServiceOrder not found"));

        Complaint complaint = Complaint.builder()
                .serviceOrder(order)
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .proofImgUrls(request.getProofImgUrls())
                .complaintType(request.getComplaintType())
                .status(ComplainStatusConstant.PENDING)
                .build();

        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    @Override
    public List<ComplaintRes> getAllComplaintsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole().equals("Customer")) {
            return complaintRepository.findAll().stream()
                    .filter(c -> c.getServiceOrder().getUser().getUserId().equals(userId))
                    .map(this::mapToResponse)
                    .toList();
        } else if (user.getRole().equals("Woodworker")) {
            return complaintRepository.findAll().stream()
                    .filter(c -> c.getServiceOrder().getAvailableService().getWoodworkerProfile().getUser().getUserId().equals(userId))
                    .map(this::mapToResponse)
                    .toList();
        }

        return null;
    }

    @Override
    public List<ComplaintRes> getAllComplaints() {
        return complaintRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<ComplaintRes> getAllComplaintsByServiceOrderId(Long serviceOrderId) {
        return complaintRepository.findAll().stream()
                .filter(c -> c.getServiceOrder().getOrderId().equals(serviceOrderId))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ComplaintRes updateComplaintByComplaintId(UpdateComplaintRequest request) {
        Complaint complaint = complaintRepository.findComplaintByComplaintId(request.getComplaintId());

        complaint.setWoodworkerResponse(request.getWoodworkerResponse());
        complaint.setUpdatedAt(LocalDateTime.now());
        complaint.setStatus(ComplainStatusConstant.IN_PROGRESS);
        complaintRepository.save(complaint);

        return mapToResponse(complaint);
    }

    @Override
    public UpdateStatusComplaintRes updateStatusByComplaintId(UpdateStatusComplaintRequest request) {
        Complaint complaint = complaintRepository.findComplaintByComplaintId(request.getComplaintId());
        User customerUser = complaint.getServiceOrder().getUser();
        User woodworkerUser = complaint.getServiceOrder().getAvailableService().getWoodworkerProfile().getUser();
        User staffUser = userRepository.findById(request.getStaffUserId()).orElseThrow();
        Wallet customerWallet = walletRepository.findByUser_UserId(customerUser.getUserId()).orElseThrow();
        Wallet woodworkerWallet = walletRepository.findByUser_UserId(woodworkerUser.getUserId()).orElseThrow();

        complaint.setStatus(ComplainStatusConstant.COMPLETED);
        complaint.setUpdatedAt(LocalDateTime.now());
        complaint.setRefundAmount(request.getRefundAmount());
        complaint.setStaffResponse(request.getStaffResponse());
        complaint.setStaffUser(staffUser);

        Transaction refundCreditTransaction = Transaction.builder()
                .transactionType(TransactionTypeConstant.HOAN_TIEN)
                .paymentFor(PaymentForConstant.REFUND_PAYMENT)
                .amount(request.getRefundAmount())
                .description("Nhận tiền hoàn lại từ đơn hàng: " + complaint.getServiceOrder().getOrderId() + "Mã khiếu nại: " + complaint.getComplaintId())
                .createdAt(LocalDateTime.now())
                .status(true)
                .user(customerUser)
                .wallet(customerWallet)
                .build();
        transactionRepository.save(refundCreditTransaction);

        Transaction refundDebitTransaction = Transaction.builder()
                .transactionType(TransactionTypeConstant.TRU_HOAN_TIEN)
                .paymentFor(PaymentForConstant.REFUND_PAYMENT)
                .amount(request.getRefundAmount())
                .description("Hoàn tiền cho đơn hàng: " + complaint.getServiceOrder().getOrderId() + "Mã khiếu nại: " + complaint.getComplaintId())
                .createdAt(LocalDateTime.now())
                .status(true)
                .user(woodworkerUser)
                .wallet(woodworkerWallet)
                .build();
        transactionRepository.save(refundDebitTransaction);

        complaint.setRefundCreditTransaction(refundCreditTransaction);
        complaint.setRefundDebitTransaction(refundDebitTransaction);
        complaintRepository.save(complaint);

        customerWallet.setBalance(customerWallet.getBalance() + request.getRefundAmount());
        walletRepository.save(customerWallet);

        woodworkerWallet.setBalance(woodworkerWallet.getBalance() - request.getRefundAmount());
        walletRepository.save(woodworkerWallet);

        return UpdateStatusComplaintRes.builder()
                .complaintId(complaint.getComplaintId())
                .updatedStatus(complaint.getStatus())
                .message("Status updated successfully")
                .build();
    }

    private ComplaintRes mapToResponse(Complaint complaint) {
        ServiceOrderDetailRes serviceOrderDetailRes = serviceOrderService.getServiceDetailById(complaint.getServiceOrder().getOrderId());

        return ComplaintRes.builder()
                .complaintId(complaint.getComplaintId())
                .complaintType(complaint.getComplaintType())
                .description(complaint.getDescription())
                .woodworkerResponse(complaint.getWoodworkerResponse())
                .staffResponse(complaint.getStaffResponse())
                .proofImgUrls(complaint.getProofImgUrls())
                .status(complaint.getStatus())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .refundAmount(complaint.getRefundAmount())
                .refundCreditTransaction(
                        complaint.getRefundCreditTransaction() == null
                                ? null
                                : modelMapper.map(complaint.getRefundCreditTransaction(), TransactionDetailRes.class)
                )
                .refundDebitTransaction(
                        complaint.getRefundDebitTransaction() == null
                                ? null
                                : modelMapper.map(complaint.getRefundDebitTransaction(), TransactionDetailRes.class)
                )
                .staffUser(
                        complaint.getStaffUser() == null
                                ? null
                                : modelMapper.map(complaint.getStaffUser(), UserDetailRes.class)
                )
                .serviceOrderDetail(serviceOrderDetailRes)
                .build();
    }
}
