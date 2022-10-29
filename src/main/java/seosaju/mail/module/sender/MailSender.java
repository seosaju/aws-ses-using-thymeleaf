package seosaju.mail.module.sender;

public interface MailSender {

    void send(String to, String subject, String content);
}
