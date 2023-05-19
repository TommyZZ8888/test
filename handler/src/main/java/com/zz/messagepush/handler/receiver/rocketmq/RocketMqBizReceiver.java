package com.zz.messagepush.handler.receiver.rocketmq;

import com.alibaba.fastjson.JSON;
import com.zz.messagepush.common.domain.dto.TaskInfo;
import com.zz.messagepush.handler.receiver.service.ConsumeService;
import com.zz.messagepush.support.constant.MessageQueuePipeline;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description RocketMqBizReceiver
 * @Author 张卫刚
 * @Date Created on 2023/5/19
 */

@Component
@ConditionalOnProperty(name = "austin-mq-pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
@RocketMQMessageListener(topic = "${austin.business.topic.name}",
        consumerGroup = "${austin-rocketmq-biz-consumer-group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "${austin.business.tagId.value}"
)
public class RocketMqBizReceiver implements RocketMQListener<String> {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        List<TaskInfo> taskInfos = JSON.parseArray(message, TaskInfo.class);
        consumeService.consume2Send(taskInfos);
    }
}
