package SP25SE026_GSP48_WDCRBP_api.model.responseModel;

import SP25SE026_GSP48_WDCRBP_api.model.entity.ServiceOrder;
import SP25SE026_GSP48_WDCRBP_api.model.entity.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComplaintRes {
    private Long complaintId;

    private String complaintType;

    private String description;

    private String woodworkerResponse;

    private String status;

    private String staffResponse;

    private LocalDateTime createdAt;

    private String proofImgUrls;

    private LocalDateTime updatedAt;

    private float refundAmount;

    private TransactionDetailRes refundCreditTransaction;

    private TransactionDetailRes refundDebitTransaction;

    private ServiceOrderDetailRes serviceOrderDetail;

    private UserDetailRes staffUser;
}
