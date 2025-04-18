package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.OrderProgressDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.OrderProgress;
import SP25SE026_GSP48_WDCRBP_api.service.OrderProgressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/OderProgress")
public class OderProgressController {
    @Autowired
    private OrderProgressService orderProgressService;

    @Autowired
    private ModelMapper modelMapper;

    public OderProgressController(OrderProgressService orderProgressService, ModelMapper modelMapper)
    {
        this.orderProgressService = orderProgressService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/service-order/{id}")
    public CoreApiResponse getAllOrderProgressByOrderId(@PathVariable Long id)
    {
        List<OrderProgress> orderProgressList = orderProgressService.getAllOrderProgressByOrderId(id);
        List<OrderProgressDto> orderProgressDtos =
                orderProgressList.stream().map(
                        orderProgress -> modelMapper.map(orderProgress, OrderProgressDto.class)).toList();

        return CoreApiResponse.success(orderProgressDtos);
    }

    @GetMapping("/guarantee-order/{id}")
    public CoreApiResponse getAllOrderProgressByGuaranteeId(@PathVariable Long id)
    {
        List<OrderProgress> orderProgressList = orderProgressService.getAllOrderProgressByGuaranteeId(id);
        List<OrderProgressDto> orderProgressDtos =
                orderProgressList.stream().map(
                        orderProgress -> modelMapper.map(orderProgress, OrderProgressDto.class)).toList();

        return CoreApiResponse.success(orderProgressDtos);
    }
}
