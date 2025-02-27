package sb.rabbitmqlogconsumer.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

import java.util.List;

@Getter
@Builder
public class LogRecordEvent {
    private String table;
    private List<LogRecord> records;

    public record LogRecord(String message, String step, String logger, String host, String correlationId, String level, String method,
                            Timestamp timestamp){
    }
}
