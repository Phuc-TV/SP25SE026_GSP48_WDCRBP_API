package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.dto.AvaliableServiceDto;
import SP25SE026_GSP48_WDCRBP_api.model.entity.ConsultantAppointment;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RequestedProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderDetailRes {
    private Long orderId;
    private Byte quantity;
    private Float totalAmount;
    private boolean isInstall;
    private String description;
    private String status;
    private String feedback;
    private Float amountPaid;
    private Float amountRemaining;
    private String role;
    private AvaliableServiceDto service;
    private UserDetailRes user;
    private List<RequestedProductDetailRes> requestedProduct;
    private ConsultantAppointmentDetailRes consultantAppointment;
    private ReviewRes review;
}
