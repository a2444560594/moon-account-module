package com.market.account.MqProducer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MqMessageService {

    private final static long timeout = 3000;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendSyncMqMessage(String message) {
        try {
            log.info("发送MQ消息：{}", message);
            rocketMQTemplate.syncSend("topic-test", message, timeout);
            log.info("发送MQ消息成功 message:{}", message);
        } catch (Exception e) {
            log.error("发送MQ消息失败 message:{} ,error:{}", message, e.getMessage(), e);
        }
    }

    public void sendAsyncMqMessage(String message) {
        try {
            log.info("发送MQ消息：{}", message);
            rocketMQTemplate.asyncSend("topic-test", message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("发送MQ消息成功 message:{} ,sendResult:{}", message, sendResult, timeout);
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("发送MQ消息失败 message:{} ,error:{}", message, throwable.getMessage(), throwable);
                }
            });
        } catch (Exception e) {
            log.error("发送MQ消息失败 message:{} ,error:{}", message, e.getMessage(), e);
        }
    }
}
