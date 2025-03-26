package SP25SE026_GSP48_WDCRBP_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendPaymentLink(String to, String paymentUrl) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("🔗 Complete Your VNPay Payment");

            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                    + "<h2 style='color: #4CAF50;'>VNPay Payment Link</h2>"
                    + "<p>Dear user,</p>"
                    + "<p>Please click the button below to complete your payment:</p>"
                    + "<a href='" + paymentUrl + "' style='display: inline-block; "
                    + "padding: 10px 20px; background-color: #4CAF50; color: white; "
                    + "text-decoration: none; border-radius: 5px;'>Pay Now</a>"
                    + "<p style='margin-top: 20px;'>If you did not request this, please ignore this email.</p>"
                    + "<hr style='margin-top: 30px;'>"
                    + "<p style='font-size: 12px; color: #888;'>VNPay Payment System</p>"
                    + "</div>";

            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send payment email: " + e.getMessage(), e);
        }
    }
}
