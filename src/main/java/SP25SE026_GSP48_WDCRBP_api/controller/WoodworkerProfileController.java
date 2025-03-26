package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkerProfileDto;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignIdeaResponse;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/ww")
public class WoodworkerProfileController {
    @Autowired
    private WoodworkerProfileService woodworkerProfileService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/listWW")
    public CoreApiResponse getAllWoodWorker()
    {
        List<WoodworkerProfileDto> ideas = woodworkerProfileService.getAllWoodWorker()
                .stream().map(idea -> modelMapper.map(idea, WoodworkerProfileDto.class))
                .toList();
        if (ideas == null)
            return CoreApiResponse.error("No data found");

        return CoreApiResponse.success(ideas);
    }

    @GetMapping("/listWW/{wwId}")
    public CoreApiResponse getWoodworkerById(@PathVariable Long wwId)
    {
        return CoreApiResponse.success(modelMapper.
                map(woodworkerProfileService.getWoodworkerById(wwId), WoodworkerProfileDto.class));
    }
}
