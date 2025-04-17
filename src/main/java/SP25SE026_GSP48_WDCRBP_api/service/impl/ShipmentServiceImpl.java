package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.GuaranteeOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Shipment;
import SP25SE026_GSP48_WDCRBP_api.repository.GuaranteeOrderRepository;
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
    @Autowired
    private GuaranteeOrderRepository guaranteeOrderRepository;

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

    @Override
    public List<Shipment> getAllShipmentByGuaranteeOrderId(Long id) {
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findGuaranteeOrderByGuaranteeOrderId(id);

        List<Shipment> shipmentList = shipmentRepository.findShipmentByGuaranteeOrder(guaranteeOrder);

        return shipmentList;
    }

    @Override
    public void updateServiceOrderShipmentOrderCode(Long serviceOrderId, String orderCode) {
        ServiceOrder serviceOrder = serviceOrderRepository.findServiceOrderByOrderId(serviceOrderId);

        List<Shipment> shipments = shipmentRepository.findShipmentByServiceOrder(serviceOrder);

        for (Shipment shipment : shipments) {
            shipment.setOrderCode(orderCode);
            shipmentRepository.save(shipment);
        }
    }

    @Override
    public void updateGuaranteeOrderShipmentOrderCode(Long serviceOrderId, String orderCode, String type) {
        GuaranteeOrder guaranteeOrder = guaranteeOrderRepository.findGuaranteeOrderByGuaranteeOrderId(serviceOrderId);

        Shipment shipment = shipmentRepository.findShipmentByGuaranteeOrder(guaranteeOrder).stream().filter(s -> s.getShipType().startsWith(type)).findFirst().orElse(null);

        if (shipment != null) {
            shipment.setOrderCode(orderCode);
            shipmentRepository.save(shipment);
        }
    }
}
