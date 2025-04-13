package SP25SE026_GSP48_WDCRBP_api.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Shipment ")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @Column(nullable = true)
    private String toAddress;

    @Column(nullable = true)
    private int toDistrictId;

    @Column(nullable = true)
    private String toWardCode;

    @Column(nullable = true)
    private String fromAddress;

    @Column(nullable = true)
    private int fromDistrictId;

    @Column(nullable = true)
    private String fromWardCode;

    @Column(nullable = true)
    private String shippingUnit;

    @Column(nullable = true)
    private float fee;

    @Column(nullable = true)
    private String orderCode;

    @Column(nullable = true)
    private String shipType;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = true)
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "guaranteeOrderId", nullable = true)
    private GuaranteeOrder guaranteeOrder;
}
