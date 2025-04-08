package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalProductDetailRes {
    private List<TechSpecDetailRes> techSpecList;
    private String designUrls;
}
