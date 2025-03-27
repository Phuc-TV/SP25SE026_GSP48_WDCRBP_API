package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkerProfileDto;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    // Lấy danh sách tất cả các Woodworker với service pack "Gold", "Silver", "Bronze"
    @GetMapping("/listWW")
    public CoreApiResponse getAllWoodWorker() {
        List<WoodworkerProfileDto> ideas = woodworkerProfileService.getAllWoodWorker()
                .stream().map(idea -> modelMapper.map(idea, WoodworkerProfileDto.class))
                .toList();
        if (ideas == null || ideas.isEmpty()) {
            return CoreApiResponse.error("No data found");
        }

        return CoreApiResponse.success(ideas);
    }

    // Lấy thông tin của một Woodworker theo ID
    @GetMapping("/listWW/{wwId}")
    public CoreApiResponse getWoodworkerById(@PathVariable Long wwId) {
        return CoreApiResponse.success(modelMapper.map(
                woodworkerProfileService.getWoodworkerById(wwId), WoodworkerProfileDto.class
        ));
    }

    // Thêm một Service Pack cho Woodworker
    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/addServicePack/{wwId}")
    public CoreApiResponse addServicePack(@PathVariable Long wwId, @RequestParam Long servicePackId) {
        WoodworkerProfileDto woodworkerProfileDto = modelMapper.map(
                woodworkerProfileService.addServicePack(servicePackId, wwId), WoodworkerProfileDto.class
        );
        return CoreApiResponse.success(woodworkerProfileDto);
    }
}
