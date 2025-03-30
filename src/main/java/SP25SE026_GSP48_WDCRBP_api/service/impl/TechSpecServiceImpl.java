package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.dto.TechSpecDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.TechSpec;
import SP25SE026_GSP48_WDCRBP_api.model.entity.TechSpecValue;
import SP25SE026_GSP48_WDCRBP_api.repository.TechSpecRepository;
import SP25SE026_GSP48_WDCRBP_api.repository.TechSpecValueRepository;
import SP25SE026_GSP48_WDCRBP_api.service.TechSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TechSpecServiceImpl implements TechSpecService {
    @Autowired
    private TechSpecRepository techSpecRepository;

    @Autowired
    private TechSpecValueRepository techSpecValueRepository;

    public TechSpecServiceImpl (TechSpecRepository techSpecRepository,
                                TechSpecValueRepository techSpecValueRepository)
    {
        this.techSpecRepository = techSpecRepository;
        this.techSpecValueRepository = techSpecValueRepository;
    }

    @Override
    public List<TechSpecDto> getAllTechSpec()
    {
        List<TechSpecDto> techSpecs = new ArrayList<>();

        List<TechSpec> techSpecList = techSpecRepository.findAll();

        for (TechSpec techSpec : techSpecList)
        {
            TechSpecDto techSpecDto = new TechSpecDto();
            techSpecDto.setTechSpecId(techSpec.getTechSpecId());
            techSpecDto.setName(techSpec.getName());
            techSpecDto.setOptionType(techSpec.getOptionType());

            List<TechSpecValue> techSpecValues =
                    techSpecValueRepository.findTechSpecValueByTechSpec(techSpec);

            List<String> values = new ArrayList<>();

            for (TechSpecValue techSpecValue : techSpecValues)
                values.add(techSpecValue.getValue());

            techSpecDto.setValues(values);

            techSpecs.add(techSpecDto);
        }
        return techSpecs;
    }
}
