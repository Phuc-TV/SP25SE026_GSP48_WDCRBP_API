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
    private String required_note;
    private String from_name;
    private String from_phone;
    private String from_address;
    private String from_ward_name;
    private String from_district_name;
    private String from_province_name;
    private String to_phone;
    private String to_name;
    private String to_address;
    private String to_ward_code;
    private String to_district_id;
    private int weight;
    private int length;
    private int width;
    private int height;
    private int service_type_id;
    private List<OrderItem> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String name;
        private int quantity;
        private int length;
        private int width;
        private int height;
        private int weight;
    }
}
