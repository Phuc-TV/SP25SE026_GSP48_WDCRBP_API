package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;

public interface GHNApiService {
    CoreApiResponse getProvinces();

    // API lấy danh sách District theo province_id
    CoreApiResponse getDistricts(int provinceId);

    // API lấy danh sách District theo district_id
    CoreApiResponse getWard(int districtId);
}
