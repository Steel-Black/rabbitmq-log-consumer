package sb.rabbitmqlogconsumer.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sb.rabbitmqlogconsumer.dto.ErrorMessage;
import sb.rabbitmqlogconsumer.repositories.EventRepository;

@Repository
@RequiredArgsConstructor
public class ErrorMessageRepository implements EventRepository<ErrorMessage> {
    private final JdbcTemplate jdbcTemplate;
    private final static String SQL = "INSERT INTO error_messages(original_message,queue, error_type, error_message, retries, message_id, timestamp) VALUES (?, ?,?, ?, ?, ?, ?)";

    @Override
    public void save(ErrorMessage errorMessage) {
        try {
            jdbcTemplate.update(SQL, errorMessage.getOriginalMessage(), errorMessage.getQueue(), errorMessage.getErrorType(), errorMessage.getErrorMessage(), errorMessage.getRetries(), errorMessage.getMessageId(), errorMessage.getTimestamp());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
        }
    }
}
