package SP25SE026_GSP48_WDCRBP_api.service;

import SP25SE026_GSP48_WDCRBP_api.model.dto.QuotationDTO;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.QuotationDetailRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.QuotationDetailRes;

import java.util.List;

public interface QuotationDetailService {
    List<QuotationDetailRes> saveQuotationDetail(QuotationDetailRequest requestDTO);
    List<QuotationDetailRes> getAllByServiceOrderId(Long serviceOrderId);
}

