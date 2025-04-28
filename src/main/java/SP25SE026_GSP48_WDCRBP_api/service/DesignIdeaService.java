package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignIdeaVariantDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.DesignUpdateDto;
import SP25SE026_GSP48_WDCRBP_api.model.dto.WoodworkProductDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;

import java.util.List;

public interface DesignIdeaService {
    List<DesignIdea> getAllDesignIdeasByWWId(Long wwId);

    DesignIdea getDesignById(Long id);

    List<DesignIdea> getAllDesignIdea();

    DesignIdea addDesignIdea(WoodworkProductDto woodworkProductDto);

    void updateDesignIdea(DesignUpdateDto dto);

    void deleteDesignIdea(Long id);

    List<DesignIdeaVariantDto> getDesignIdeaVariantByDesignId(Long designId);
}
