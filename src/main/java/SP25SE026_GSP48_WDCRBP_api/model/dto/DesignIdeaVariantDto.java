package SP25SE026_GSP48_WDCRBP_api.model.dto;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdea;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariant;
import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaVariantConfig;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesignIdeaVariantDto {
    private Long designIdeaVariantId;

    private Float price;

    private DesignIdea designIdea;

    private List<DesignIdeaVariantConfig> designIdeaVariantConfig;
}
