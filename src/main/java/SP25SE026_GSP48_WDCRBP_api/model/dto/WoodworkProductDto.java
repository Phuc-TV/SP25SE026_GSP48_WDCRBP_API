package SP25SE026_GSP48_WDCRBP_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoodworkProductDto {
    private Boolean isInstall;
    private Long woodworkerId;
    private String name;
    private String img;
    private String description;
    private Long categoryId;
    private List<ConfigurationDto> configurations;
    private List<PriceDto> prices;
}
