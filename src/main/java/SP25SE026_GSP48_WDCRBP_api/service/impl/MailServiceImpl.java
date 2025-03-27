package SP25SE026_GSP48_WDCRBP_api.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl {

    private final JavaMailSender mailSender;

    // Method to send different types of emails (password, payment, OTP, etc.)
    public void sendEmail(String to, String subject, String messageType, String linkOrPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = "";

            // Check the message type and apply the appropriate HTML content
            if ("password".equalsIgnoreCase(messageType)) {
                // Password email content
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                        + "<h2 style='color: #4CAF50;'>Your Woodworker Account Password</h2>"
                        + "<p>Dear user,</p>"
                        + "<p>Your account password is: <strong>" + linkOrPassword + "</strong></p>"
                        + "<p>Thank you for using our service.</p>"
                        + "<p>If you did not request this email, please contact support immediately.</p>"
                        + "<hr style='margin-top: 30px;'>"
                        + "<p style='font-size: 12px; color: #888;'>Woodworker Service</p>"
                        + "</div>";
            } else if ("payment".equalsIgnoreCase(messageType)) {
                // Payment link email content
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                        + "<h2 style='color: #4CAF50;'>VNPay Payment Link</h2>"
                        + "<p>Dear user,</p>"
                        + "<p>Please click the button below to complete your payment:</p>"
                        + "<a href='" + linkOrPassword + "' style='display: inline-block; "
                        + "padding: 10px 20px; background-color: #4CAF50; color: white; "
                        + "text-decoration: none; border-radius: 5px;'>Pay Now</a>"
                        + "<p style='margin-top: 20px;'>If you did not request this, please ignore this email.</p>"
                        + "<hr style='margin-top: 30px;'>"
                        + "<p style='font-size: 12px; color: #888;'>VNPay Payment System</p>"
                        + "</div>";
            } else if ("otp".equalsIgnoreCase(messageType)) {
                // OTP email content
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                        + "<h2 style='color: #4CAF50;'>Your Password Reset OTP</h2>"
                        + "<p>Dear user,</p>"
                        + "<p>Your OTP for password reset is: <strong>" + linkOrPassword + "</strong></p>"
                        + "<p>If you did not request this, please contact support immediately.</p>"
                        + "<hr style='margin-top: 30px;'>"
                        + "<p style='font-size: 12px; color: #888;'>Woodworker Service</p>"
                        + "</div>";
            }

            // Set the email content as HTML
            helper.setText(htmlContent, true);

            // Send the email
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}

