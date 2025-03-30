package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.dto.*;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignIdeaResponse;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.DesignIdeaService;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DesignIdeaServiceImpl implements DesignIdeaService {
    @Autowired
    private DesignIdeaRepository ideaRepository;

    @Autowired
    private WoodworkerProfileService woodworkerProfileService;

    @Autowired
    private DesignIdeaConfigRepository designIdeaConfigRepository;

    @Autowired
    private DesignIdeaConfigValueRepository designIdeaConfigValueRepository;

    @Autowired
    private DesignIdeaVariantRepository designIdeaVariantRepository;

    @Autowired
    private DesignIdeaVariantConfigRepository designIdeaVariantConfigRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DesignIdeaRepository designIdeaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public DesignIdeaServiceImpl(
            DesignIdeaRepository ideaRepository,
            WoodworkerProfileService woodworkerProfileService,
            DesignIdeaConfigRepository designIdeaConfigRepository,
            DesignIdeaConfigValueRepository designIdeaConfigValueRepository,
            DesignIdeaVariantRepository designIdeaVariantRepository,
            DesignIdeaVariantConfigRepository designIdeaVariantConfigRepository,
            CategoryRepository categoryRepository,
            ModelMapper modelMapper
    ) {
        this.ideaRepository = ideaRepository;
        this.woodworkerProfileService = woodworkerProfileService;
        this.designIdeaConfigRepository = designIdeaConfigRepository;
        this.designIdeaConfigValueRepository = designIdeaConfigValueRepository;
        this.designIdeaVariantRepository = designIdeaVariantRepository;
        this.designIdeaVariantConfigRepository = designIdeaVariantConfigRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<DesignIdea> getAllDesignIdeasByWWId(Long wwId)
    {
        WoodworkerProfile woodworkerProfile = woodworkerProfileService.getWoodworkerById(wwId);

        if (woodworkerProfile == null)
            return null;

        return ideaRepository.findDesignIdeaByWoodworkerProfile(woodworkerProfile);
    }

    @Override
    public DesignIdea getDesignById(Long id)
    {
        return ideaRepository.findDesignIdeaByDesignIdeaId(id);
    }

    @Override
    public List<DesignIdea> getAllDesignIdea()
    {
        List<DesignIdea> ideas = new ArrayList<>();

        List<WoodworkerProfile> woodworkerProfiles = woodworkerProfileService.getAllWoodWorker();
        for (WoodworkerProfile woodworkerProfile : woodworkerProfiles)
        {
            List<DesignIdea> idea = ideaRepository.findDesignIdeaByWoodworkerProfile(woodworkerProfile);

            ideas.addAll(idea);
        }

        return ideas;
    }

    @Override
    public DesignIdea addDesignIdea(WoodworkProductDto woodworkProductDto)
    {
        HashMap<Integer, DesignIdeaConfigValue> configValueHashMap = new HashMap<Integer,DesignIdeaConfigValue>();
        Category category = categoryRepository.findCategoriesByCategoryId(woodworkProductDto.getCategoryId());

        // Save DesignIdea
        DesignIdea idea = new DesignIdea();
        idea.setCategory(category);
        idea.setName(woodworkProductDto.getName());
        idea.setImg_urls(woodworkProductDto.getImg());
        ideaRepository.save(idea);

        //Save DesignIdeaConfig and DesignIdeaConfigValue
        List<ConfigurationDto> configurationDtos = woodworkProductDto.getConfigurations();

        for (ConfigurationDto configurationDto : configurationDtos)
        {
            DesignIdeaConfig designIdeaConfig = new DesignIdeaConfig();
            designIdeaConfig.setSpecifications(configurationDto.getName());
            designIdeaConfig.setDesignIdea(idea);

            designIdeaConfigRepository.save(designIdeaConfig);

            DesignIdeaConfig designIdeaConfig1 =
                    designIdeaConfigRepository.findDesignIdeaConfigBySpecifications(configurationDto.getName());

            List<ConfigValueDto> configValueDtos = configurationDto.getValues();
            for (ConfigValueDto configValueDto : configValueDtos)
            {
                DesignIdeaConfigValue designIdeaConfigValue = new DesignIdeaConfigValue();
                designIdeaConfigValue.setValue(configValueDto.getName());
                designIdeaConfigValue.setDesignIdeaConfig(designIdeaConfig1);

                designIdeaConfigValueRepository.save(designIdeaConfigValue);

                configValueHashMap.put(configValueDto.getId(), designIdeaConfigValue);
            }
        }

        //Save DesignIdeaVariant and DesignIdeaVariantConfig
        List<PriceDto> prices = woodworkProductDto.getPrices();

        for (PriceDto priceDto : prices)
        {
            DesignIdeaVariant designIdeaVariant = new DesignIdeaVariant();
            designIdeaVariant.setPrice(priceDto.getPrice());
            designIdeaVariant.setDesignIdea(idea);
            designIdeaVariantRepository.save(designIdeaVariant);

            List<Integer> configValueList = priceDto.getConfigValue();
            for (Integer configValueId : configValueList)
            {
                DesignIdeaVariantConfig designIdeaVariantConfig = new DesignIdeaVariantConfig();
                designIdeaVariantConfig.setDesignIdeaVariant(designIdeaVariant);
                designIdeaVariantConfig.setDesignIdeaConfigValue(configValueHashMap.get(configValueId));

                designIdeaVariantConfigRepository.save(designIdeaVariantConfig);
            }
        }

        return idea;
    }

    @Override
    public List<DesignIdeaVariantDto> getDesignIdeaVariantByDesignId(Long designId)
    {
        DesignIdea designIdea = designIdeaRepository.findDesignIdeaByDesignIdeaId(designId);

        List<DesignIdeaVariant> designIdeaVariants =
                designIdeaVariantRepository.findDesignIdeaVariantByDesignIdea(designIdea);

        List<DesignIdeaVariantDto> designIdeaVariantDtos = new ArrayList<>();

        if (designIdeaVariants == null)
            return null;

        for (DesignIdeaVariant designIdeaVariantDtoss : designIdeaVariants)
        {
            List<DesignIdeaVariantConfig> designIdeaVariantConfigs =
                    designIdeaVariantConfigRepository.
                            findDesignIdeaVariantConfigByDesignIdeaVariant(designIdeaVariantDtoss);

            DesignIdeaVariantDto designIdeaVariantDto =
                    modelMapper.map(designIdeaVariantDtoss, DesignIdeaVariantDto.class);

            designIdeaVariantDto.setDesignIdeaVariantConfig(designIdeaVariantConfigs);

            designIdeaVariantDtos.add(designIdeaVariantDto);
        }

        return designIdeaVariantDtos;
    }
}
