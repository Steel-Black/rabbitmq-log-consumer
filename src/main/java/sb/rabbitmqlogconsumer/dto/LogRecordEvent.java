package sb.rabbitmqlogconsumer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sb.rabbitmqlogconsumer.utils.annotations.SqlInjectionCheck;

import java.sql.Timestamp;
import java.util.List;


@Data
public class LogRecordEvent {
    @NotBlank(message = "Table name cannot be blank")
    @SqlInjectionCheck(message = "Table name contains forbidden sql keywords")
    private String table;
    @Size(min = 1, message = "Records list must contain at least one element")
    @Valid
    private List<LogRecord> records;

    public record LogRecord(
            String message,
            @NotBlank(message = "Step cannot be blank")
            String step,
            @NotBlank(message = "Logger cannot be blank")
            String logger,
            @NotBlank(message = "Host cannot be blank")
            String host,
            @NotBlank(message = "Correlation ID cannot be blank")
            String correlationId,
            @NotBlank(message = "Level cannot be blank")
            String level,
            @NotBlank(message = "Method cannot be blank")
            String method,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            @NotNull(message = "Timestamp cannot be null")
            Timestamp timestamp) {
    }
}
