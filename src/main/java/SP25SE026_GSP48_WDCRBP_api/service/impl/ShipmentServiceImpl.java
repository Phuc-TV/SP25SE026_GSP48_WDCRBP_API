package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Shipment;
import SP25SE026_GSP48_WDCRBP_api.repository.ServiceOrderRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.ShipmentRepository;
import SP25SE026_GSP48_WDCRBP_api.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    public ShipmentServiceImpl(ShipmentRepository shipmentRepository,
                               ServiceOrderRepository serviceOrderRepository)
    {
        this.shipmentRepository = shipmentRepository;
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Override
    public List<Shipment> getAllShipmentByServiceOrderId(Long serviceOrderId)
    {
        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByOrderId(serviceOrderId);

        List<Shipment> shipment = shipmentRepository.findShipmentByServiceOrder(serviceOrder);

        return shipment;
    }
}
