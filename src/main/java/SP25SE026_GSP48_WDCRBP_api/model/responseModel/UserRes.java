package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.Data;

@Data
public class UserRes {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private Boolean status;
}
