package sb.rabbitmqlogconsumer.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sb.rabbitmqlogconsumer.dto.LogRecordEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class LogEventEventRepository implements EventRepository<LogRecordEvent> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(LogRecordEvent logRecordEvent) {
        //String message, String step,String logger,String host,String correlation_id,String level,Timestamp time_stamp, String method
        String sql = String.format("INSERT INTO %s (correlation_id, host, method, step, message, logger, level, time_stamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", logRecordEvent.getTable());

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LogRecordEvent.LogRecord logRecord = logRecordEvent.getRecords().get(i);
                ps.setString(1, logRecord.correlationId());
                ps.setString(2, logRecord.host());
                ps.setString(3, logRecord.method());
                ps.setString(4, logRecord.step());
                ps.setString(5, logRecord.message());
                ps.setString(6, logRecord.logger());
                ps.setString(7, logRecord.level());
                ps.setTimestamp(8, logRecord.timestamp());
            }

            @Override
            public int getBatchSize() {
                return logRecordEvent.getRecords().size();
            }
        });

    }

    @Override
    public void saveAll(List<LogRecordEvent> list) {
        String sql = "INSERT INTO %s (level, message, timestamp, source) VALUES (?, ?, ?, ?)";


    }
}
