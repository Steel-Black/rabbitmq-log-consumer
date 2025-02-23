package sb.rabbitmqlogconsumer.config.queue;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Getter
@Service
@Setter
public abstract class QueueSettingSkeleton {
    private String name;
    private String exchange;
    private Map<String,Object> arguments = new HashMap<>();
}
