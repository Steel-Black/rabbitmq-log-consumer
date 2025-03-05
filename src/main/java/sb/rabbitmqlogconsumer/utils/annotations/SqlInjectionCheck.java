package sb.rabbitmqlogconsumer.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SqlInjectionCheckValidator.class)
public @interface SqlInjectionCheck {
    String message() default "Field contains forbidden keywords";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}