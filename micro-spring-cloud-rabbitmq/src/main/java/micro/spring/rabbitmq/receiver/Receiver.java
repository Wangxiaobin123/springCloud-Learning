package micro.spring.rabbitmq.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 定义该类对hello队列的监听
 *
 * @Author: shengbin
 * @since: 2019/4/28 下午6:10
 */
@Component
@RabbitListener(queues = "hello")
@Slf4j
public class Receiver {

    @RabbitHandler
    public void process(String hello) {
        log.info("Receiver = {}", hello);
    }

}
