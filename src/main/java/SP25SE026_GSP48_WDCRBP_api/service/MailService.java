package SP25SE026_GSP48_WDCRBP_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendPaymentLink(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your VNPay Payment Link");
        message.setText("Click the link below to complete your payment:\n\n" + link);
        mailSender.send(message);
    }
}
