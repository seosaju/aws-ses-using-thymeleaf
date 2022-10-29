package seosaju.mail.module.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SesMailService implements MailService {

    private final MailSender mailSender;

    @Override
    public void send(String to, String subject, String content) {

        mailSender.send(to, subject, content);
    }
}
