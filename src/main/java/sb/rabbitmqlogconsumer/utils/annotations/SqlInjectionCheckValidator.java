package sb.rabbitmqlogconsumer.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SqlInjectionCheckValidator implements ConstraintValidator<SqlInjectionCheck, String> {

    private static final Pattern FORBIDDEN_KEYWORDS_PATTERN = Pattern.compile(
            "(?i)\\b(INSERT|UPDATE|SELECT|DELETE|TRUNCATE|DROP)\\b"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Предполагается, что валидация на пустое значение осуществляется другой аннотацией, например, @NotBlank
        }
        return !FORBIDDEN_KEYWORDS_PATTERN.matcher(value).find();
    }
}
