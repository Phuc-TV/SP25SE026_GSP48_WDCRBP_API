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
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @GetMapping
    public CoreApiResponse<List<ComplaintRes>> getAllComplaints() {
        try {
            List<ComplaintRes> result = complaintService.getAllComplaints();
            return CoreApiResponse.success(result, "Lấy danh sách tất cả khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi lấy danh sách khiếu nại");
        }
    }

    @GetMapping("/user/{id}")
    public CoreApiResponse<List<ComplaintRes>> getAllComplaintsByUserId(@PathVariable Long id) {
        try {
            List<ComplaintRes> result = complaintService.getAllComplaintsByUserId(id);
            return CoreApiResponse.success(result, "Lấy danh sách tất cả khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống khi lấy danh sách khiếu nại");
        }
    }

    @GetMapping("/service-order/{id}")
    public CoreApiResponse<List<ComplaintRes>> getAllComplaintsByServiceOrderId(@PathVariable Long id) {
        try {
            List<ComplaintRes> result = complaintService.getAllComplaintsByServiceOrderId(id);
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

    @PutMapping("/staff")
    public CoreApiResponse<UpdateStatusComplaintRes> updateComplaintByStaff(@RequestBody UpdateStatusComplaintRequest request) {
        try {
            UpdateStatusComplaintRes result = complaintService.updateStatusByComplaintId(request);
            return CoreApiResponse.success(result, "Cập nhật trạng thái khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Không thể cập nhật trạng thái khiếu nại: " + e.getMessage());
        }
    }

    @PutMapping("/woodworker")
    public CoreApiResponse<ComplaintRes> updateComplaint(@RequestBody UpdateComplaintRequest request) {
        try {
            ComplaintRes result = complaintService.updateComplaintByComplaintId(request);
            return CoreApiResponse.success(result, "Cập nhật nội dung khiếu nại thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.NOT_FOUND, "Không thể cập nhật khiếu nại: " + e.getMessage());
        }
    }
}

