package sb.rabbitmqlogconsumer.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorMessage {
    private final String originalMessage;
    private final String errorType;
    private final String queue;
    private final String errorMessage;
    private final int retries;
    private final String messageId;
    private final LocalDateTime timestamp;
}
