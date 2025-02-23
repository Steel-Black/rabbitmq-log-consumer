package sb.rabbitmqlogconsumer.consumer.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
import sb.rabbitmqlogconsumer.config.queue.extention.DlqLogSettings;
import sb.rabbitmqlogconsumer.config.queue.extention.RetryLogQueueSettings;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RabbitErrorHandler implements RabbitListenerErrorHandler {

    private final RabbitTemplate rabbitTemplate;
    private final RetryLogQueueSettings retryLogQueueSettings;
    private final DlqLogSettings dlqLogSettings;

    @Override
    public Object handleError(Message message, Channel channel, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {

        System.err.println("Error processing message: " + Arrays.toString(message.getBody()));
        System.err.println("Exception: " + e.getCause().getMessage());
        // Проверяем тип ошибки
        if (e.getCause() instanceof IllegalArgumentException) {
            // Ошибка валидации → отправляем в DLQ
            System.out.println("Sending message to DLQ...");
            rabbitTemplate.send(dlqLogSettings.getExchange(), "", message);
        } else {
            // Временная ошибка → повторяем обработку
            System.out.println("Retrying message processing...");
            rabbitTemplate.send(retryLogQueueSettings.getExchange(), "", message);
        }
        return null;
    }
}

