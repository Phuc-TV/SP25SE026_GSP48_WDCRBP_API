package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.CalculateFeeRequest;

public interface GHNApiService {
    CoreApiResponse getProvinces();

    // API lấy danh sách District theo province_id
    CoreApiResponse getDistricts(int provinceId);

    // API lấy danh sách District theo district_id
    CoreApiResponse getWard(int districtId);

    CoreApiResponse calculateShippingFee(CalculateFeeRequest request);
}
