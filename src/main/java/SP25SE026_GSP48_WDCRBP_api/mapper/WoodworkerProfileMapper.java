package SP25SE026_GSP48_WDCRBP_api.mapper;

import SP25SE026_GSP48_WDCRBP_api.model.entity.WoodworkerProfile;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileDetailRes;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.WoodworkerProfileListItemRes;

public class WoodworkerProfileMapper {

    public static WoodworkerProfileDetailRes toDetailRes(WoodworkerProfile entity) {
        if (entity == null) return null;

        WoodworkerProfileDetailRes dto = new WoodworkerProfileDetailRes();

        dto.setWoodworkerId(entity.getWoodworkerId());
        dto.setBrandName(entity.getBrandName());
        dto.setBio(entity.getBio());
        dto.setBusinessType(entity.getBusinessType());
        dto.setTaxCode(entity.getTaxCode());
        dto.setImgUrl(entity.getImgUrl());
        dto.setAddress(entity.getAddress());
        dto.setWardCode(entity.getWardCode());
        dto.setDistrictId(entity.getDistrictId());
        dto.setCityId(entity.getCityId());
        dto.setTotalStar(entity.getTotalStar());
        dto.setTotalReviews(entity.getTotalReviews());
        dto.setServicePackStartDate(entity.getServicePackStartDate());
        dto.setServicePackEndDate(entity.getServicePackEndDate());

        dto.setUser(UserMapper.toDetailRes(entity.getUser()));

        dto.setServicePack(entity.getServicePack());

        return dto;
    }

    public static WoodworkerProfileListItemRes toListItemRes(WoodworkerProfile entity) {
        if (entity == null) return null;

        WoodworkerProfileListItemRes dto = new WoodworkerProfileListItemRes();

        dto.setWoodworkerId(entity.getWoodworkerId());
        dto.setBrandName(entity.getBrandName());
        dto.setImgUrl(entity.getImgUrl());
        dto.setTotalStar(entity.getTotalStar());
        dto.setTotalReviews(entity.getTotalReviews());
        dto.setAddress(entity.getAddress());
        dto.setCityId(entity.getCityId());
        dto.setServicePack(entity.getServicePack());

        return dto;
    }
}
