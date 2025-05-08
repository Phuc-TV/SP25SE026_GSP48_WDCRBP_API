package SP25SE026_GSP48_WDCRBP_api.service.impl;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailServiceImpl {

    @Value("${sendgrid.api-key}")
    private String sendgridApiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    public void sendEmail(String to, String subject, String messageType, String linkOrPassword) {
        String htmlContent = generateEmailContent(messageType, linkOrPassword);

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("Email sent: " + response.getStatusCode());
        } catch (IOException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private String generateEmailContent(String type, String data) {
        String title;
        String recipientName = "bạn";
        String messageBody;
        String coreContent;

        switch (type.toLowerCase()) {
            case "otp":
                title = "<strong>Mã xác thực OTP</strong>";
                messageBody = "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi. Hãy sử dụng mã OTP dưới đây để hoàn tất quy trình xác thực.";
                coreContent = "<p style='margin-top: 60px; font-size: 40px; font-weight: 600; letter-spacing: 25px; color: #ba3d4f;'>"
                        + data + "</p>";
                break;
            case "password":
                title = "Mật khẩu truy cập hệ thống";
                messageBody = "Mật khẩu truy cập hệ thống của bạn là:";
                coreContent = "<p style='margin-top: 40px; font-size: 24px; font-weight: bold; color: #1f1f1f;'>"
                        + data + "</p>";
                break;
            case "payment":
                title = "Yêu cầu thanh toán đơn hàng";
                messageBody = "Hãy nhấp vào nút bên dưới để tiến hành thanh toán đơn hàng của bạn.";
                coreContent = "<a href='" + data + "' style='display:inline-block;margin-top:40px;padding:12px 30px;background-color:#ba3d4f;color:#fff;font-weight:500;border-radius:8px;text-decoration:none;'>Thanh toán ngay</a>";
                break;
            case "status-rejection":
                title = "Cập nhật trạng thái bị từ chối";
                recipientName = "xưởng mộc";
                messageBody = "Yêu cầu cập nhật trạng thái của bạn đã bị từ chối.<br><br><strong>Lý do:</strong> " + data;
                coreContent = "";
                break;
            case "buy-pack-success":
                title = "Gói dịch vụ đã được kích hoạt";
                recipientName = "xưởng mộc";
                messageBody = "Cảm ơn bạn đã mua gói dịch vụ. Gói của bạn là:";
                coreContent = "<p style='margin-top: 40px; font-size: 20px; font-weight: bold; color: #1f1f1f;'>"
                        + data + "</p>";
                break;
            default:
                title = "Thông báo hệ thống";
                messageBody = "Đây là thông báo từ hệ thống.";
                coreContent = "";
        }

        return "<!DOCTYPE html>"
                + "<html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                + "<link href='https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap' rel='stylesheet'>"
                + "<title>Email Notification</title></head>"
                + "<body style='margin: 0; font-family: Poppins, sans-serif; background: #ffffff; font-size: 14px;'>"
                + "<div style='max-width: 680px; margin: 0 auto; padding: 45px 30px 60px; background: #f4f7ff; "
                + "background-image: url(https://archisketch-resources.s3.ap-northeast-2.amazonaws.com/vrstyler/1661497957196_595865/email-template-background-banner);"
                + "background-repeat: no-repeat; background-size: 800px 452px; background-position: top center; color: #434343;'>"

                + "<header><table style='width: 100%;'><tr><td>"
                + "<img src='https://i.imgur.com/Ke4MgOK.png' height='30px' alt='Logo'/>"
                + "</td><td style='text-align: right;'><span style='font-size: 16px; line-height: 30px; color: #ffffff;'>"
                + java.time.LocalDate.now()
                + "</span></td></tr></table></header>"

                + "<main><div style='margin-top: 70px; padding: 92px 30px 115px; background: #ffffff; border-radius: 30px; text-align: center;'>"
                + "<div style='max-width: 489px; margin: 0 auto;'>"
                + "<h1 style='font-size: 24px; font-weight: 500; color: #1f1f1f; margin-bottom: 20px;'>" + title + "</h1>"
                + "<p style='font-size: 16px; font-weight: 500;'>Chào " + recipientName + ",</p>"
                + "<p style='margin-top: 17px; font-weight: 500; letter-spacing: 0.56px;'>" + messageBody + "</p>"
                + coreContent
                + "</div></div>"

                + "<p style='max-width: 400px; margin: 90px auto 0; text-align: center; font-weight: 500; color: #8c8c8c;'>"
                + "Cần trợ giúp? Gửi email đến <a href='mailto:support@yourplatform.vn' style='color: #499fb6;'>namndpse171442@fpt.edu.vn</a>"
                + "</p></main>"

                + "<footer style='max-width: 490px; margin: 20px auto 0; text-align: center; border-top: 1px solid #e6ebf1;'>"
                + "<p style='margin-top: 40px; font-size: 16px; font-weight: 600;'>Nền tảng đồ gỗ</p>"
                + "<p style='margin-top: 8px;'>Địa chỉ: 123 Đường Gỗ Việt, Q.Tân Bình, TP.HCM</p>"
                + "<div style='margin-top: 16px;'>"
                + "</div><p style='margin-top: 16px;'>© 2025 Nền tảng đồ gỗ. All rights reserved.</p></footer></div></body></html>";
    }
}
