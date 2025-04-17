package SP25SE026_GSP48_WDCRBP_api.controller;

import SP25SE026_GSP48_WDCRBP_api.components.CoreApiResponse;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GuaranteeOrderIdRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.GuaranteeQuotationDetailRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.ServiceOrderIdRequest;
import SP25SE026_GSP48_WDCRBP_api.model.requestModel.QuotationDetailRequest;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.QuotationDetailRes;
import SP25SE026_GSP48_WDCRBP_api.service.QuotationDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotation")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationDetailService quotationService;

    @PostMapping("/save-quotation-details")
    public CoreApiResponse<List<QuotationDetailRes>> saveQuotationDetails(
            @Valid @RequestBody QuotationDetailRequest request) {

        try{
        List<QuotationDetailRes> response = quotationService.saveQuotationDetail(request);
        return CoreApiResponse.success(response, "Quotation details processed successfully");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi cố gắng thêm: " + e.getMessage());
        }
    }

    @PostMapping("/get-by-service-order")
    public CoreApiResponse<List<QuotationDetailRes>> getAllByServiceOrderId(
            @Valid @RequestBody ServiceOrderIdRequest request) {

        try {
            List<QuotationDetailRes> response = quotationService.getAllByServiceOrderId(request.getServiceOrderId());
            return CoreApiResponse.success(response, "Fetched all quotation details by service order");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi tải thông tin báo giá: " + e.getMessage());
        }
    }

    @PostMapping("guarantee-order/save-quotation-details")
    public CoreApiResponse saveGuaranteeQuotationDetails(
            @Valid @RequestBody GuaranteeQuotationDetailRequest request) {
        try{
            quotationService.saveQuotationDetailForGuarantee(request);
            return CoreApiResponse.success("Quotation details processed successfully");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi cố gắng thêm: " + e.getMessage());
        }
    }

    @PostMapping("guarantee-order/get-by-service-order")
    public CoreApiResponse<QuotationDetailRes> getAllByGuaranteeOrderId(
            @Valid @RequestBody GuaranteeOrderIdRequest request) {
        try {
            QuotationDetailRes response = quotationService.getAllByGuaranteeOrderId(request.getGuaranteeOrderId());
            return CoreApiResponse.success(response, "Fetched all quotation details by service order");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi tải thông tin báo giá: " + e.getMessage());
        }
    }

    @PutMapping("guarantee-order/accept")
    public CoreApiResponse<QuotationDetailRes> acceptQuotationForGuaranteeOrder(
            @Valid @RequestBody GuaranteeOrderIdRequest request) {
        try {
            quotationService.acceptQuotation(request.getGuaranteeOrderId());
            return CoreApiResponse.success("Thành công");
        } catch (Exception e) {
            return CoreApiResponse.error(HttpStatus.BAD_REQUEST, "Lỗi khi thao tác dữ liệu");
        }
    }
}
