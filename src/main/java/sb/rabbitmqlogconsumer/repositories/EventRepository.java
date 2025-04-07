package sb.rabbitmqlogconsumer.repositories;

public interface EventRepository<T> {
    void save(T t);
}
