package seosaju.mail.module.sender;

public interface MailService {

    void send(String to, String subject, String content);
}
