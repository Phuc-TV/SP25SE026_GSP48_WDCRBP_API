package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.*;
import SP25SE026_GSP48_WDCRBP_api.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @GetMapping("/{serviceOrderId}")
    public CoreApiResponse<ComplaintRes> getByServiceOrderId(@PathVariable Long serviceOrderId) {
        try {
            ComplaintRes result = complaintService.getComplaintByServiceOrderId(serviceOrderId);
            return CoreApiResponse.success(result, "Lấy khiếu nại theo mã đơn hàng thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Không tìm thấy khiếu nại với đơn hàng ID: " + serviceOrderId);
        }
    }

    @GetMapping
    public CoreApiResponse<List<ComplaintRes>> getAllComplaints() {
        try {
            List<ComplaintRes> result = complaintService.getAllComplaints();
            return CoreApiResponse.success(result, "Lấy danh sách tất cả khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi lấy danh sách khiếu nại");
        }
    }

    @PostMapping
    public CoreApiResponse<ComplaintRes> createComplaint(@RequestBody CreateComplaintRequest request) {
        try {
            ComplaintRes result = complaintService.createComplaint(request);
            return CoreApiResponse.success(result, "Tạo khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Tạo khiếu nại thất bại: " + e.getMessage());
        }
    }

    @PutMapping("/status")
    public CoreApiResponse<UpdateStatusComplaintRes> updateStatus(@RequestBody UpdateStatusComplaintRequest request) {
        try {
            UpdateStatusComplaintRes result = complaintService.updateStatusByServiceOrderId(request);
            return CoreApiResponse.success(result, "Cập nhật trạng thái khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Không thể cập nhật trạng thái khiếu nại: " + e.getMessage());
        }
    }

    @PutMapping
    public CoreApiResponse<ComplaintRes> updateComplaint(@RequestBody UpdateComplaintRequest request) {
        try {
            ComplaintRes result = complaintService.updateComplaintByServiceOrderId(request);
            return CoreApiResponse.success(result, "Cập nhật nội dung khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Không thể cập nhật khiếu nại: " + e.getMessage());
        }
    }
}

