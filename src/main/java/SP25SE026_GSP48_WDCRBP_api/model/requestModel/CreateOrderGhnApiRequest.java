package SP25SE026_GSP48_WDCRBP_api.model.requestModel;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderGhnApiRequest {
    private int payment_type_id;
    private String note;
    private String required_note;
    private String from_name;
    private String from_phone;
    private String from_address;
    private String from_ward_name;
    private String from_district_name;
    private String from_province_name;
    private String return_phone;
    private String return_address;
    private Integer return_district_id;
    private String return_ward_code;
    private String client_order_code;
    private String to_name;
    private String to_phone;
    private String to_address;
    private String to_ward_code;
    private int to_district_id;
    private int cod_amount;
    private String content;
    private int weight;
    private int length;
    private int width;
    private int height;
    private Integer pick_station_id;
    private Integer deliver_station_id;
    private int insurance_value;
    private int service_id;
    private int service_type_id;
    private String coupon;
    private List<Integer> pick_shift;
    private List<OrderItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String name;
        private String code;
        private int quantity;
        private int price;
        private int length;
        private int width;
        private int height;
        private int weight;
        private Map<String, String> category; // e.g. { "level1": "Áo" }
    }
}
