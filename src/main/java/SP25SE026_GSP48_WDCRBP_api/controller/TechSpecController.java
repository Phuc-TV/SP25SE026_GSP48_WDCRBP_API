package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.dto.TechSpecDto;
import SP25SE026_GSP48_WDCRBP_api.service.TechSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tech-spec")
@CrossOrigin(origins = "*")
public class TechSpecController {
    @Autowired
    private TechSpecService techSpecService;

    public TechSpecController(TechSpecService techSpecService)
    {
        this.techSpecService = techSpecService;
    }

    @GetMapping("/listAllTechSpec")
    public CoreApiResponse getAllTechSpecService()
    {
        List<TechSpecDto> techSpecDtos = techSpecService.getAllTechSpec();
        return CoreApiResponse.success(techSpecDtos);
    }
}
