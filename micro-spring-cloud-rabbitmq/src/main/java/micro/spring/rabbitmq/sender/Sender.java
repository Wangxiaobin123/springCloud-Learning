package micro.spring.rabbitmq.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * sender
 *
 * @Author: shengbin
 * @since: 2019/4/28 下午6:07
 */
@Component
@Slf4j
public class Sender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello " + new Date();
        log.info("send = {}", context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }
}
