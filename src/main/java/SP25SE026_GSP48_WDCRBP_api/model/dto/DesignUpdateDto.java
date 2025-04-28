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
public class DesignUpdateDto {
    private Boolean isInstall;
    private Long designIdeaId;
    private String name;
    private String img;
    private String description;
    private List<VariantPriceDto> prices;
}
