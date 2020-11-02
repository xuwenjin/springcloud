package com.xwj.rabbitmq;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

/**
 * 消息生产者
 */
@Service
public class MqSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 发送Direct消息
	 */
	public void sendDirectMessage() {
		String msg = buildMsgStr();

		// DIRECT_ROUTINGKEY发送到DIRECT_EXCHANGE交换机
		rabbitTemplate.convertAndSend(MqConsts.DIRECT_EXCHANGE, MqConsts.DIRECT_ROUTINGKEY, msg);
	}

	/**
	 * 发送Topic消息
	 */
	public void sendTopicMessage() {
		String msg = buildMsgStr();

		// TOPIC_EXCHANGE上绑定的routeKey为topic.key1，则TOPIC_QUEUE1和TOPIC_QUEUE2都可以收到消息
		// rabbitTemplate.convertAndSend(MqConsts.TOPIC_EXCHANGE, "topic.key1",
		// msg + "1");

		// TOPIC_EXCHANGE上绑定的routeKey为topic.key2，则只有TOPIC_QUEUE2可以收到消息
		rabbitTemplate.convertAndSend(MqConsts.TOPIC_EXCHANGE, "topic.key2", msg + "2");
	}

	/**
	 * 发送Fanout消息
	 */
	public void sendFanoutMessage() {
		String msg = buildMsgStr();
		rabbitTemplate.convertAndSend(MqConsts.FUNOUT_EXCHANGE, null, msg);
	}

	/**
	 * 构建消息体
	 */
	private String buildMsgStr() {
		String messageId = String.valueOf(UUID.randomUUID());
		String messageData = "test message, hello!";
		String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		Map<String, Object> map = new HashMap<>();
		map.put("messageId", messageId);
		map.put("messageData", messageData);
		map.put("createTime", createTime);

		return JSON.toJSONString(map);
	}

}
