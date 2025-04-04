package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Complaint;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
import SP25SE026_GSP48_WDCRBP_api.repository.ComplaintRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.UserRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final UserRepository userRepository;

    @Override
    public ComplaintRes getComplaintByServiceOrderId(Long serviceOrderId) {
        Complaint complaint = complaintRepository.findAll().stream()
                .filter(c -> c.getServiceOrder().getOrderId().equals(serviceOrderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        return mapToResponse(complaint);
    }

    @Override
    public ComplaintRes createComplaint(CreateComplaintRequest request) {
        ServiceOrder order = serviceOrderRepository.findById(request.getServiceOrderId())
                .orElseThrow(() -> new RuntimeException("ServiceOrder not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Complaint complaint = Complaint.builder()
                .user(user)
                .serviceOrder(order)
                .description(request.getDescription())
                .status(false)
                .createdAt(LocalDateTime.now())
                .build();

        complaint = complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    @Override
    public UpdateStatusComplaintRes updateStatusByServiceOrderId(UpdateStatusComplaintRequest request) {
        Complaint complaint = complaintRepository.findAll().stream()
                .filter(c -> c.getServiceOrder().getOrderId().equals(request.getServiceOrderId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(request.getStatus());
        complaint.setUpdatedAt(LocalDateTime.now());
        complaintRepository.save(complaint);

        return UpdateStatusComplaintRes.builder()
                .complaintId(complaint.getComplaintId())
                .updatedStatus(complaint.getStatus())
                .message("Status updated successfully")
                .build();
    }

    @Override
    public ComplaintRes updateComplaintByServiceOrderId(UpdateComplaintRequest request) {
        Complaint complaint = complaintRepository.findAll().stream()
                .filter(c -> c.getServiceOrder().getOrderId().equals(request.getServiceOrderId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setDescription(request.getDescription());
        complaint.setUpdatedAt(LocalDateTime.now());
        complaintRepository.save(complaint);

        return mapToResponse(complaint);
    }

    @Override
    public List<ComplaintRes> getAllComplaints() {
        return complaintRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ComplaintRes mapToResponse(Complaint complaint) {
        return ComplaintRes.builder()
                .complaintId(complaint.getComplaintId())
                .userId(complaint.getUser().getUserId())
                .description(complaint.getDescription())
                .status(complaint.getStatus())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .serviceOrderId(complaint.getServiceOrder().getOrderId())
                .build();
    }
}
