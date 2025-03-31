package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkProductDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignIdeaDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignIdeaListItemRes;
import SP25SE026_GSP48_WDCRBP_api.service.DesignIdeaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/designIdea")
public class DesignIdeaController {
    @Autowired
    private DesignIdeaService designIdeaService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/getAllDesignIdeasByWWId/{wwId}")
    public CoreApiResponse getAllDesignIdeasByWWId(@PathVariable Long wwId)
    {
        try {
            List<DesignIdeaListItemRes> ideas = designIdeaService.getAllDesignIdeasByWWId(wwId)
                    .stream().map(idea -> modelMapper.map(idea, DesignIdeaListItemRes.class))
                    .toList();

            return CoreApiResponse.success(ideas);
        } catch (Exception e) {
            return CoreApiResponse.error("Không tìm thấy dữ liệu");
        }
    }

    @GetMapping("/getDesignById/{Id}")
    public CoreApiResponse getDesignById(@PathVariable Long Id)
    {
        try {
            return CoreApiResponse.success(modelMapper.map(designIdeaService.getDesignById(Id), DesignIdeaDetailRes.class));
        } catch (Exception e) {
            return CoreApiResponse.error("Không tìm thấy dữ liệu");
        }
    }

    @GetMapping("/getAllDesignIdea")
    public CoreApiResponse getAllDesignIdea()
    {
        try {
            List<DesignIdeaListItemRes> ideas = designIdeaService.getAllDesignIdea()
                    .stream().map(idea -> modelMapper.map(idea, DesignIdeaListItemRes.class))
                    .toList();

            return CoreApiResponse.success(ideas);
        } catch (Exception e) {
            return CoreApiResponse.error("Không tìm thấy dữ liệu");
        }
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/addDesignIdea")
    public CoreApiResponse addDesignIdea(@RequestBody WoodworkProductDto woodworkProductDto)
    {
        try {
            DesignIdea idea = designIdeaService.addDesignIdea(woodworkProductDto);

            if (idea == null)
                return CoreApiResponse.error("Lỗi thất bại");

            return CoreApiResponse.success(idea);
        } catch (Exception e) {
            return CoreApiResponse.error("Lỗi thất bại");
        }
    }


    @GetMapping("/getDesignIdeaVariantByDesignId/{designId}")
    public CoreApiResponse getDesignIdeaVariantByDesignId(@PathVariable Long designId)
    {
        try {
            List<DesignIdeaVariantDto> ideass = designIdeaService.getDesignIdeaVariantByDesignId(designId);

            if (ideass == null)
                return CoreApiResponse.error("Không tìm thấy dữ liệu");

            return CoreApiResponse.success(ideass);
        } catch (Exception e) {
            return CoreApiResponse.error("Không tìm thấy dữ liệu");
        }
    }
}
