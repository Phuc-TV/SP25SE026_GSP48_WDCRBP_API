package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ConsultantAppointment;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.repository.ConsultantAppointmentRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ConsultantAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultantAppointmentServiceImpl implements ConsultantAppointmentService {
    @Autowired
    private ConsultantAppointmentRepository consultantAppointmentRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    public ConsultantAppointmentServiceImpl(ConsultantAppointmentRepository consultantAppointmentRepository)
    {
        this.consultantAppointmentRepository = consultantAppointmentRepository;
    }

    @Override
    public ConsultantAppointment getAppoimentByOrderId(Long orderId)
    {
        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByOrderId(orderId);
        ConsultantAppointment consultantAppointment = serviceOrder.getConsultantAppointment();

        return consultantAppointment;
    }
}
