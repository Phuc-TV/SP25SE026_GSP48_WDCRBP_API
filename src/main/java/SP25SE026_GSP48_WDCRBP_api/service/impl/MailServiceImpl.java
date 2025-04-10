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

    public void sendEmail(String to, String subject, String messageType, String linkOrPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = generateEmailContent(messageType, linkOrPassword);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private String generateEmailContent(String type, String data) {
        String title;
        String recipientName = "người dùng";
        String contentBody;
        String actionButton = "";

        switch (type.toLowerCase()) {
            case "password":
                title = "Mật khẩu tài khoản của bạn";
                contentBody = "Mật khẩu tài khoản của bạn là:<br><br>"
                        + "<strong style='font-size: 18px;'>" + data + "</strong>";
                break;
            case "payment":
                title = "Thanh toán đơn hàng";
                contentBody = "Vui lòng nhấn nút bên dưới để hoàn tất thanh toán.";
                actionButton = "<a href='" + data + "' style='display:inline-block;padding:12px 24px;"
                        + "background-color:#4CAF50;color:#fff;text-decoration:none;"
                        + "border-radius:5px;margin-top:20px;'>Thanh toán ngay</a>";
                break;
            case "otp":
                title = "OTP Xác thực";
                contentBody = "Mã OTP của bạn là:<br><br>"
                        + "<strong style='font-size: 24px; color: #4CAF50;'>" + data + "</strong>";
                break;
            case "status-rejection":
                title = "Yêu cầu cập nhật trạng thái bị từ chối";
                recipientName = "thợ mộc";
                contentBody = "Rất tiếc, yêu cầu cập nhật trạng thái của bạn đã bị từ chối.<br><br><strong>Lý do:</strong> " + data;
                break;
            case "buy-pack-success":
                title = "Mua gói dịch vụ thành công";
                recipientName = "thợ mộc";
                contentBody = "Bạn đã mua gói dịch vụ thành công!<br><br><strong>Gói:</strong> " + data;
                break;
            default:
                title = "Thông báo từ hệ thống";
                contentBody = "Đây là email thông báo mặc định.";
                break;
        }

        return "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'><title>Email</title>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #F5F5F5; margin: 0; padding: 0; }"
                + ".container { max-width: 1000px; margin: 20px auto; background-color: #fff; border-radius: 8px; overflow: hidden; }"
                + ".header, .footer { background-color: #E9E9E9; padding: 15px; text-align: center; }"
                + ".content { padding: 30px; }"
                + ".content h2 { color: #333; margin-top: 0; }"
                + ".content p { color: #555; font-size: 15px; line-height: 1.6; }"
                + "a.button { display:inline-block; padding: 12px 24px; background-color:#4CAF50; color:#fff; text-decoration:none; border-radius:6px; }"
                + "</style></head><body>"
                + "<div class='container'>"
                + "<div class='header'><h1>Nền tảng Đồ Gỗ</h1></div>"
                + "<div class='content'>"
                + "<h2>Xin chào, " + recipientName + "!</h2>"
                + "<p><strong>" + title + "</strong></p>"
                + "<p>" + contentBody + "</p>"
                + actionButton
                + "<p style='margin-top:30px;'>Nếu bạn không yêu cầu điều này, vui lòng bỏ qua email này.</p>"
                + "</div>"
                + "<div class='footer'><p>© 2025 Nền tảng đồ gỗ. All rights reserved.</p></div>"
                + "</div></body></html>";
    }
}
