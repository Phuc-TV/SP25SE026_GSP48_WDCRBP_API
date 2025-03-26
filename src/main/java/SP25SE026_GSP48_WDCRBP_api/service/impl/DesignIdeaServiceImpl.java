package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.dto.ConfigValueDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.ConfigurationDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.PriceDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkProductDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.*;
import SP25SE026_GSP48_WDCRBP_api.repository.*;
import SP25SE026_GSP48_WDCRBP_api.service.DesignIdeaService;
import SP25SE026_GSP48_WDCRBP_api.service.WoodworkerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public DesignIdeaServiceImpl(
            DesignIdeaRepository ideaRepository,
            WoodworkerProfileService woodworkerProfileService,
            DesignIdeaConfigRepository designIdeaConfigRepository,
            DesignIdeaConfigValueRepository designIdeaConfigValueRepository,
            DesignIdeaVariantRepository designIdeaVariantRepository,
            DesignIdeaVariantConfigRepository designIdeaVariantConfigRepository,
            CategoryRepository categoryRepository
    ) {
        this.ideaRepository = ideaRepository;
        this.woodworkerProfileService = woodworkerProfileService;
        this.designIdeaConfigRepository = designIdeaConfigRepository;
        this.designIdeaConfigValueRepository = designIdeaConfigValueRepository;
        this.designIdeaVariantRepository = designIdeaVariantRepository;
        this.designIdeaVariantConfigRepository = designIdeaVariantConfigRepository;
        this.categoryRepository = categoryRepository;
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
        // Save DesignIdea
        DesignIdea idea = new DesignIdea();

        idea.setName(woodworkProductDto.getName());
        idea.setImg_urls(woodworkProductDto.getImg());

        Category category = categoryRepository.findCategoriesByCategoryId(woodworkProductDto.getCategoryId());

        idea.setCategory(category);

        ideaRepository.save(idea);

        DesignIdea designIdea = ideaRepository.findDesignIdeaByName(idea.getName());

        //Save DesignIdeaConfig and DesignIdeaConfigValue
        List<ConfigurationDto> configurationDtos = woodworkProductDto.getConfigurations();

        for (ConfigurationDto configurationDto : configurationDtos)
        {
            DesignIdeaConfig designIdeaConfig = new DesignIdeaConfig();
            designIdeaConfig.setSpecifications(configurationDto.getName());
            designIdeaConfig.setDesignIdea(designIdea);

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
            }
        }

        //Save DesignIdeaVariant and DesignIdeaVariantConfig
        List<PriceDto> prices = woodworkProductDto.getPrices();

        for (PriceDto priceDto : prices)
        {
            DesignIdeaVariant designIdeaVariant = new DesignIdeaVariant();
            designIdeaVariant.setPrice(priceDto.getPrice());

            List<DesignIdeaVariant> designIdeaVariants = designIdeaVariantRepository.findAll();
            int t = designIdeaVariants.size();

            designIdeaVariantRepository.save(designIdeaVariant);

            DesignIdeaVariant designIdeaVariant1 = designIdeaVariants.get(t+1);

            List<Integer> config = priceDto.getConfig();
            List<Integer> configVale = priceDto.getConfigValue();

            int i = 0;
            for (int index = 0; index < config.size(); index++)
            {
                List<ConfigurationDto> configurationDtoss = woodworkProductDto.getConfigurations();
                for (ConfigurationDto configurationDto : configurationDtoss)
                {
                    if (configurationDto.getId() == config.get(i))
                    {
                        DesignIdeaConfig designIdeaConfig =
                                designIdeaConfigRepository.findDesignIdeaConfigBySpecifications(configurationDto.getName());
                        List<ConfigValueDto> configValueDtos = configurationDto.getValues();
                        for (ConfigValueDto configValueDto : configValueDtos)
                        {
                            if (configValueDto.getId() == configVale.get(i))
                            {
                                DesignIdeaVariantConfig designIdeaVariantConfig = new DesignIdeaVariantConfig();
                                designIdeaVariantConfig.setDesignIdeaVariant(designIdeaVariant1);

                                DesignIdeaConfigValue designIdeaConfigValue =
                                        designIdeaConfigValueRepository.findDesignIdeaConfigValueByDesignIdeaConfig(designIdeaConfig);
                                designIdeaVariantConfig.setDesignIdeaConfigValue(designIdeaConfigValue);

                                designIdeaVariantConfigRepository.save(designIdeaVariantConfig);
                            }
                        }
                        i++;
                        break;
                    }
                }
            }
        }

        return idea;
    }
}
