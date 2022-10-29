package seosaju.mail.module.sender;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MailSenderRequest {

    @Getter
    @Setter
    public static class Send {

        @NotNull
        @EmailCollection
        private List<String> to;

        private SubscriptionType subscriptionType;
    }
}
