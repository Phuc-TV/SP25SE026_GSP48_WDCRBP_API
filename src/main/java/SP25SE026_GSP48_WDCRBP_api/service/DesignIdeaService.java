package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkProductDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariant;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.DesignIdeaResponse;

import java.util.List;

public interface DesignIdeaService {
    List<DesignIdea> getAllDesignIdeasByWWId(Long wwId);

    DesignIdea getDesignById(Long id);

    List<DesignIdea> getAllDesignIdea();

    DesignIdea addDesignIdea(WoodworkProductDto woodworkProductDto);

    List<DesignIdeaVariantDto> getDesignIdeaVariantByDesignId(Long designId);
}
