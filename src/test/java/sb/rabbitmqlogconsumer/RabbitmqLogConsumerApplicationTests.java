package sb.rabbitmqlogconsumer;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

//@SpringBootTest
class RabbitmqLogConsumerApplicationTests {

    @Test
    void contextLoads() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
    }

}
