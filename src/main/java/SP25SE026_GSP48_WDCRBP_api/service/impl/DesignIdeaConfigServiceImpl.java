package SP25SE026_GSP48_WDCRBP_api.service.impl;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfig;
import SP25SE026_GSP48_WDCRBP_api.repository.DesignIdeaConfigRepository;
import SP25SE026_GSP48_WDCRBP_api.service.DesignIdeaConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DesignIdeaConfigServiceImpl implements DesignIdeaConfigService {
    @Autowired
    private DesignIdeaConfigRepository designIdeaConfigRepository;

    public DesignIdeaConfigServiceImpl(DesignIdeaConfigRepository designIdeaConfigRepository)
    {
        this.designIdeaConfigRepository = designIdeaConfigRepository;
    }
}
