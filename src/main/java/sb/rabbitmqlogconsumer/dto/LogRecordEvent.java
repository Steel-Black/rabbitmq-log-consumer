package sb.rabbitmqlogconsumer.dto;

import java.sql.Timestamp;
import java.util.Collection;

public class LogRecordEvent {
    private String table;
    private Collection<LogRecord> records;
    record LogRecord(String message, String step, String logger, String host, String correlationId, String level,
                            Timestamp timestamp){
    }
}
