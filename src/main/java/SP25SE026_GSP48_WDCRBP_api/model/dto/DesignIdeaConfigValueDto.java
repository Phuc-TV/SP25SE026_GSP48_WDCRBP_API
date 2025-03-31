package SP25SE026_GSP48_WDCRBP_api.model.dto;

import SP25SE026_GSP48_WDCRBP_api.model.entity.DesignIdeaConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesignIdeaConfigValueDto {
    private Long designIdeaConfigValueId;

    private String value;

    private DesignIdeaConfigDto designIdeaConfig;
}
