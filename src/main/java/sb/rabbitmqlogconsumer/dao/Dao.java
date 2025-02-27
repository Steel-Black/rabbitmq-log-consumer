package sb.rabbitmqlogconsumer.dao;

import java.util.List;

public interface Dao<T> {

    void save(T t);

    void saveAll(List<T> list);
}
