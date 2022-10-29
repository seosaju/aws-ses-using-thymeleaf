package seosaju.mail.client.aws.ses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import seosaju.mail.module.sender.MailSender;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
@Component
public class SmtpMailSender implements MailSender {

    @Value("${aws.ses.smtp.host}")
    private String host;

    @Value("${aws.ses.smtp.port}")
    private int port;

    @Value("${aws.ses.smtp.username}")
    private String username;

    @Value("${aws.ses.smtp.password}")
    private String password;

    @Value("${aws.ses.from}")
    private String fromEmail;

    @Value("${aws.ses.from-name}")
    private String fromName;

    @Override
    public void send(String to, String subject, String content) {

        Properties properties = System.getProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties);

        MimeMessage mimeMessage;
        try {
            Address address = new InternetAddress(to);

            mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(fromEmail, fromName));
            mimeMessage.setRecipient(Message.RecipientType.TO, address);
            mimeMessage.setSubject(subject, StandardCharsets.UTF_8.name());
            mimeMessage.setContent(content, "text/html;charset=UTF-8");
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        try (Transport transport = session.getTransport()) {
            log.info("Sending...");

            transport.connect(host, username, password);

            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            log.info("Email Sent!");
        } catch (MessagingException e) {
            log.error("The email was not sent.");
            log.error("Error message: " + e.getMessage());
        }
    }
}
