package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuaranteeOrderDetailRes {
    private Long guaranteeOrderId;
    private Float totalAmount;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String productCurrentStatus;
    private String currentProductImgUrls;
    private Float shipFee;
    private boolean isInstall;
    private String status;
    private Float amountPaid;
    private Float amountRemaining;
    private RequestedProductListItemRes requestedProduct;
    private String role;
    private String feedback;
    private WoodworkerProfileListItemRes woodworker;
    private UserDetailRes user;
    private UserDetailRes woodworkerUser;
    private ServiceOrderDetailRes serviceOrderDetail;
    private ConsultantAppointmentDetailRes consultantAppointment;
    private ReviewRes review;
}
