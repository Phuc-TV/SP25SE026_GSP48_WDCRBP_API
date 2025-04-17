package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.entity.Shipment;

import java.util.List;

public interface ShipmentService {
    List<Shipment> getAllShipmentByServiceOrderId(Long serviceOrderId);
    List<Shipment> getAllShipmentByGuaranteeOrderId(Long id);
    void updateServiceOrderShipmentOrderCode(Long serviceOrderId, String orderCode);
    void updateGuaranteeOrderShipmentOrderCode(Long serviceOrderId, String orderCode, String type);
}
