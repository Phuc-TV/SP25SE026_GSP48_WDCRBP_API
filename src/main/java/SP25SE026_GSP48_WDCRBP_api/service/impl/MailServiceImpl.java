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
                        + "<h2 style='color: #4CAF50;'>Mật khẩu tài khoản thợ mộc của bạn</h2>"
                        + "<p>Kính gửi người dùng,</p>"
                        + "<p>Mật khẩu tài khoản của bạn là: <strong>" + linkOrPassword + "</strong></p>"
                        + "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>"
                        + "<p>Nếu bạn không yêu cầu điều này, vui lòng liên hệ ngay với bộ phận hỗ trợ.</p>"
                        + "<hr style='margin-top: 30px;'>"
                        + "<p style='font-size: 12px; color: #888;'>Nền tảng đặt dịch vụ thiết kế, chế tác & sửa chữa đồ gỗ</p>"
                        + "</div>";
            } else if ("payment".equalsIgnoreCase(messageType)) {
                // Payment link email content
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                        + "<h2 style='color: #4CAF50;'>Hệ thống thanh toán VNPay</h2>"
                        + "<p>Kính gửi người dùng,</p>"
                        + "<p>Vui lòng nhấp vào nút bên dưới để hoàn tất thanh toán:</p>"
                        + "<a href='" + linkOrPassword + "' style='display: inline-block; "
                        + "padding: 10px 20px; background-color: #4CAF50; color: white; "
                        + "text-decoration: none; border-radius: 5px;'>Trả ngay</a>"
                        + "<p style='margin-top: 20px;'>Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>"
                        + "<hr style='margin-top: 30px;'>"
                        + "<p style='font-size: 12px; color: #888;'>Nền tảng đặt dịch vụ thiết kế, chế tác & sửa chữa đồ gỗ</p>"
                        + "</div>";
            } else if ("otp".equalsIgnoreCase(messageType)) {
                // OTP email content
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                        + "<h2 style='color: #4CAF50;'>OTP của bạn tới rồi đây!</h2>"
                        + "<p>Kính gửi người dùng,</p>"
                        + "<p>OTP của bạn là: <strong>" + linkOrPassword + "</strong></p>"
                        + "<p>Nếu bạn không yêu cầu điều này, vui lòng liên hệ ngay với bộ phận hỗ trợ.</p>"
                        + "<hr style='margin-top: 30px;'>"
                        + "<p style='font-size: 12px; color: #D17B49;'>Nền tảng đặt dịch vụ thiết kế, chế tác & sửa chữa đồ gỗ</p>"
                        + "</div>";
            } else if ("status-rejection".equalsIgnoreCase(messageType)) {
                // Email for status rejection
                htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px;'>"
                        + "<h2 style='color: #F44336;'>Yêu cầu cập nhật trạng thái không được chấp nhận</h2>"
                        + "<p>Kính gửi thợ mộc,</p>"
                        + "<p>Chúng tôi rất tiếc phải thông báo rằng yêu cầu cập nhật trạng thái của bạn đã bị từ chối.</p>"
                        + "<p><strong>Lý do:</strong> " + linkOrPassword + "</p>"
                        + "<p>Vui lòng kiểm tra lại thông tin hoặc liên hệ bộ phận hỗ trợ để biết thêm chi tiết.</p>"
                        + "<hr style='margin-top: 30px;'>"
                        + "<p style='font-size: 12px; color: #888;'>Nền tảng đặt dịch vụ thiết kế, chế tác & sửa chữa đồ gỗ</p>"
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

