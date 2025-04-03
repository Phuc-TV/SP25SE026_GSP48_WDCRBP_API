package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceOrderDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ShipmentDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Shipment;
import SP25SE026_GSP48_WDCRBP_api.service.ShipmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/Shipment")
public class ShipmentController {
    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/getAllShipmentByServiceOrderId/{serviceOrderId}")
    public CoreApiResponse getAllShipmentByServiceOrderId(@PathVariable Long serviceOrderId)
    {
        List<Shipment> shipments = shipmentService.getAllShipmentByServiceOrderId(serviceOrderId);

        List<ShipmentDto> shipmentDtos = shipments.
                stream().map(shipment -> modelMapper.map(shipment, ShipmentDto.class)).collect(Collectors.toList());

        return CoreApiResponse.success(shipmentDtos);
    }
}
