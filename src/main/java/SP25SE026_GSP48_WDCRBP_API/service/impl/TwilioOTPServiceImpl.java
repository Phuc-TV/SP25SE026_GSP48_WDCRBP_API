package SP25SE026_GSP48_WDCRBP_API.service.impl;

import SP25SE026_GSP48_WDCRBP_API.model.entity.Configuration;
import SP25SE026_GSP48_WDCRBP_API.service.ConfigurationService;
import SP25SE026_GSP48_WDCRBP_API.service.TwilioOTPService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TwilioOTPServiceImpl implements TwilioOTPService {
    @Autowired
    private ConfigurationService configurationService;

    String twilioAccountSidValue;
    String twilioAuthTokenValue;
    String twilioVerifyServiceSidValue;

    @Autowired
    public TwilioOTPServiceImpl(ConfigurationService configurationService) {
        // Initialize Twilio SDK
        this.configurationService = configurationService;
    }


    @Override
    // Start a new verification
    public Verification startVerification(String toPhoneNumber) {
        List<Configuration> configurations = (List<Configuration>) configurationService.getAllConfiguration().getData();
        Optional<Configuration> twilioAccountSid = configurations.stream()
                .filter(config -> "Twilio_Account_sid".equals(config.getDescription()))
                .findFirst();
        Optional<Configuration> twilioAuthToken = configurations.stream()
                .filter(config -> "Twilio_Auth_Token".equals(config.getDescription()))
                .findFirst();
        Optional<Configuration> twilioVerifyServiceSid = configurations.stream()
                .filter(config -> "TTwilio_VerifyService_Sid".equals(config.getDescription()))
                .findFirst();

        if (twilioAccountSid.isPresent()) {
            twilioAccountSidValue = twilioAccountSid.get().getValue();
        } else {
            throw new RuntimeException("Không tìm thấy cấu hình với description là 'Twilio_Account_sid'");
        }

        if (twilioAuthToken.isPresent()) {
            twilioAuthTokenValue = twilioAuthToken.get().getValue();
        } else {
            throw new RuntimeException("Không tìm thấy cấu hình với description là 'Twilio_Account_sid'");
        }

        if (twilioVerifyServiceSid.isPresent()) {
            twilioVerifyServiceSidValue = twilioVerifyServiceSid.get().getValue();
        } else {
            throw new RuntimeException("Không tìm thấy cấu hình với description là 'Twilio_Account_sid'");
        }

        Twilio.init(twilioAccountSidValue, twilioAuthTokenValue);

        return Verification.creator(
                twilioVerifyServiceSidValue,
                convertToInternationalFormat(toPhoneNumber),
                "sms"
        ).create();
    }

    @Override
    // Check a verification
    public VerificationCheck checkVerification(String toPhoneNumber, String code) {
        Twilio.init(twilioAccountSidValue,twilioAuthTokenValue);

        return VerificationCheck.creator(
                        twilioVerifyServiceSidValue
                ).setTo(convertToInternationalFormat(toPhoneNumber))
                .setCode(code)
                .create();
    }

    public String convertToInternationalFormat(String phoneNumber)
    {
        if (phoneNumber.startsWith("0")) {
            return phoneNumber.replaceFirst("0", "+84");
        }
        return phoneNumber;
    }


    @Override
    // Fetch a specific verification status
    public Verification fetchVerification(String sid) {
        return Verification.fetcher(twilioVerifyServiceSidValue, sid).fetch();
    }
}
