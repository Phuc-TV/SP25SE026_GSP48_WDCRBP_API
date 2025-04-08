package SP25SE026_GSP48_WDCRBP_api.model.dto;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ConsultantAppointment;
import SP25SE026_GSP48_WDCRBP_api.model.entity.RequestedProduct;
import SP25SE026_GSP48_WDCRBP_api.model.responseModel.UserDetailRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderDto {
    private Long orderId;
    private Byte quantity;
    private Float totalAmount;
    private boolean isInstall;
    private String description;
    private String status;
    private String feedback;
    private Float amountPaid;
    private Float amountRemaining;
    private LocalDateTime createdAt;
    private String role;
    private AvaliableServiceDto service;
    private UserDetailRes user;
}
