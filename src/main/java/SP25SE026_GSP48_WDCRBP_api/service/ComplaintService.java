package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CreateComplaintRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateComplaintRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.UpdateStatusComplaintRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.ComplaintRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UpdateStatusComplaintRes;

import java.util.List;

public interface ComplaintService {
    ComplaintRes createComplaint(CreateComplaintRequest request);

    List<ComplaintRes> getAllComplaints();

    List<ComplaintRes> getAllComplaintsByServiceOrderId(Long serviceOrderId);

    List<ComplaintRes> getAllComplaintsByUserId(Long userId);

    ComplaintRes updateComplaintByComplaintId(UpdateComplaintRequest request);

    UpdateStatusComplaintRes updateStatusByComplaintId(UpdateStatusComplaintRequest request);
}
