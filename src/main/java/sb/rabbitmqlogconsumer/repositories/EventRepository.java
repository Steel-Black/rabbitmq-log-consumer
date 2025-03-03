package sb.rabbitmqlogconsumer.repositories;

import java.util.List;

public interface EventRepository<T> {

    void save(T t);

    void saveAll(List<T> list);
}
