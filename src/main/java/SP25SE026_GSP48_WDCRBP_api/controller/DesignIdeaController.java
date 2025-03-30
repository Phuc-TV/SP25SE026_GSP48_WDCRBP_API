package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkProductDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignIdeaResponse;
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
        List<DesignIdeaResponse> ideas = designIdeaService.getAllDesignIdeasByWWId(wwId)
                .stream().map(idea -> modelMapper.map(idea, DesignIdeaResponse.class))
                .toList();
        if (ideas == null)
            return CoreApiResponse.error("No data found");

        return CoreApiResponse.success(ideas);
    }

    @GetMapping("/getDesignById/{Id}")
    public CoreApiResponse getDesignById(@PathVariable Long Id)
    {
        return CoreApiResponse.success(designIdeaService.getDesignById(Id));
    }

    @GetMapping("/getAllDesignIdea")
    public CoreApiResponse getAllDesignIdea()
    {
        List<DesignIdea> ideass = designIdeaService.getAllDesignIdea();

        List<DesignIdeaResponse> ideas = designIdeaService.getAllDesignIdea()
                .stream().map(idea -> modelMapper.map(idea, DesignIdeaResponse.class))
                .toList();

        if (ideass == null)
            return CoreApiResponse.error("No data found");

        return CoreApiResponse.success(ideass);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/addDesignIdea")
    public CoreApiResponse addDesignIdea(@RequestBody WoodworkProductDto woodworkProductDto)
    {
        DesignIdea idea = designIdeaService.addDesignIdea(woodworkProductDto);

        if (idea == null)
            return CoreApiResponse.error("Error");
        return CoreApiResponse.success(idea);
    }


    @GetMapping("/getDesignIdeaVariantByDesignId/{designId}")
    public CoreApiResponse getDesignIdeaVariantByDesignId(@PathVariable Long designId)
    {
        List<DesignIdeaVariantDto> ideass = designIdeaService.getDesignIdeaVariantByDesignId(designId);

        if (ideass == null)
            return CoreApiResponse.error("No data found");

        return CoreApiResponse.success(ideass);
    }
}
