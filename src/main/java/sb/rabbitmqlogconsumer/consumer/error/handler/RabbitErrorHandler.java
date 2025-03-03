package sb.rabbitmqlogconsumer.consumer.error.handler;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.config.settings.extention.DlqLogSettings;
import sb.rabbitmqlogconsumer.config.settings.extention.LogQueueSetting;
import sb.rabbitmqlogconsumer.config.settings.extention.RetryLogQueueSettings;

@Component
@RequiredArgsConstructor
public class RabbitErrorHandler implements RabbitListenerErrorHandler {

    private final RabbitTemplate rabbitTemplate;
    private final RetryLogQueueSettings retryLogQueueSettings;
    private final DlqLogSettings dlqLogSettings;
    private final LogQueueSetting logQueueSetting;

    @Override
    public Object handleError(Message message, Channel channel, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) {
        System.err.println("Exception: " + e.getCause().getMessage());
        // Проверяем тип ошибки
        if (e.getCause() instanceof IllegalArgumentException) {
            // Ошибка валидации → отправляем в DLQ
            System.out.println("Sending message to DLQ...");
            rabbitTemplate.send(dlqLogSettings.getExchange(), "", message);
        } else if (message.getMessageProperties().getConsumerQueue().equals(logQueueSetting.getName())) {
            System.out.println("Send message to retry queue...");
            rabbitTemplate.send(retryLogQueueSettings.getExchange(), "", message);
        } else {
            System.out.println("Retrying message processing...");
            throw e;
        }
        return null;
    }

}

