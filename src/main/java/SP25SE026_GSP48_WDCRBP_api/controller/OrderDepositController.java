package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ServiceDepositDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderDeposit;
import SP25SE026_GSP48_WDCRBP_api.service.OrderDepositService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/OrderDeposit")
public class OrderDepositController {
    @Autowired
    private OrderDepositService orderDepositService;

    @Autowired
    private ModelMapper modelMapper;

    public OrderDepositController(OrderDepositService orderDepositService,
                                  ModelMapper modelMapper)
    {
        this.orderDepositService = orderDepositService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/getAllOrderDepositByOrderId/{id}")
    public CoreApiResponse<List<ServiceDepositDto>> getAllOrderDepositByOrderId(@PathVariable Long id)
    {
        List<OrderDeposit> orderDeposits = orderDepositService.getAllOrderDepositByOrderId(id);

        List<ServiceDepositDto> serviceDepositDtos = orderDeposits.
                stream().map(orderDeposit -> modelMapper.map(orderDeposit, ServiceDepositDto.class)).toList();

        return CoreApiResponse.success(serviceDepositDtos);
    }
}
