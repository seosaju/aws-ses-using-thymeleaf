package seosaju.mail.module.sender;

import org.springframework.validation.annotation.Validated;

import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Validated(EmailCollectionValidator.class)
@Documented
public @interface EmailCollection {

    String message() default "{javax.validation.constraints.Email.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
