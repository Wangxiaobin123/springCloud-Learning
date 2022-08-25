package micro.rabbitmq;

import micro.spring.rabbitmq.MicroSpringRabbitMqMain;
import micro.spring.rabbitmq.sender.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: shengbin
 * @since: 2019/4/28 下午6:15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MicroSpringRabbitMqMain.class)
public class RabbitMqTests {
    @Autowired
    private Sender sender;

    @Test
    public void hello() {
        sender.send();
    }
}
