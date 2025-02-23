package sb.rabbitmqlogconsumer.config.queue.extention;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.config.queue.QueueSettingSkeleton;
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq.queues.dlq-log")
public class DlqLogSettings extends QueueSettingSkeleton {
}
