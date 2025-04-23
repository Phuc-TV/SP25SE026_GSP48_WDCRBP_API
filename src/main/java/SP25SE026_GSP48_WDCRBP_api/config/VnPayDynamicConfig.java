package SP25SE026_GSP48_WDCRBP_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "vnpay")
public class VnPayDynamicConfig {
    private String tmnCode;
    private String version;
    private String command;
    private String secretKey;
    public static final String PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
}
