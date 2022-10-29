package seosaju.mail.module.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("mail")
public class MailSenderController {

    private final MailService mailService;
    private final TemplateEngine templateEngine;

    @PostMapping
    public ResponseEntity<Void> sendSubscriptionExpireMail(@Validated @RequestBody MailSenderRequest.Send request) {

        int threads = Runtime.getRuntime().availableProcessors() + 1;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);

        request.getTo().forEach(to -> {
            executorService.submit(() -> {
                Context context = getSubscriptionExpireMailContext(request.getSubscriptionType());
                String html = templateEngine.process("subscription-expire", context);

                mailService.send(to, (String) context.getVariable("title"), html);
            });
        });

        executorService.shutdown();

        return null;
    }

    private Context getSubscriptionExpireMailContext(SubscriptionType subscriptionType) {

        Context context = new Context();

        int daysToAdd;
        String title;

        switch (subscriptionType) {
            case MONTHLY:
                daysToAdd = 10;
                title = "월간 구독 만료 안내입니다.";
                break;
            case ANNUALLY:
                daysToAdd = 25;
                title = "연간 구독 만료 안내입니다.";
                break;
            default:
                throw new IllegalStateException("처리할 수 없는 메일 타입입니다.");
        }

        context.setVariable("expireDate", LocalDate.now().plusDays(daysToAdd));
        context.setVariable("title", title);

        return context;
    }
}
