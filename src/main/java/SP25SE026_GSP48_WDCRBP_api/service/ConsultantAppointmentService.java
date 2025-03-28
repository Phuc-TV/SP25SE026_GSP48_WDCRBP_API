package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ConsultantAppointment;

public interface ConsultantAppointmentService {
    ConsultantAppointment getAppoimentByOrderId(Long orderId);
}
