package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.entity.User;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;

public class UserMapper {
    public static UserDetailRes toDetailRes(User user) {
        if (user == null) return null;

        UserDetailRes dto = new UserDetailRes();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }
}