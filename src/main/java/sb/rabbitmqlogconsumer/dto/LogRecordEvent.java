package sb.rabbitmqlogconsumer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Builder
public class LogRecordEvent implements Serializable {
    @NotBlank
    private String table;
    @Size(min = 1)
    private List<LogRecord> records;

    public record LogRecord(String message, @NotBlank String step,@NotBlank String logger,@NotBlank String host, @NotBlank String correlationId, @NotBlank String level, @NotBlank String method,
                           @NotNull Timestamp timestamp){
    }
}
