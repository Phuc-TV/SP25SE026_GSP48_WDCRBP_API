package SP25SE026_GSP48_WDCRBP_api.service;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

public interface TwilioOTPService {
    // Start a new verification
    Verification startVerification(String toPhoneNumber);

    // Check a verification
    VerificationCheck checkVerification(String toPhoneNumber, String code);

    // Fetch a specific verification status
    Verification fetchVerification(String sid);
}
