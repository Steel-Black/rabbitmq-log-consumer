package sb.rabbitmqlogconsumer.config.settings.extention;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import sb.rabbitmqlogconsumer.config.settings.QueueSettingSkeleton;
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq.queues.dlq-log")
public class DlqLogSettings extends QueueSettingSkeleton {
}
