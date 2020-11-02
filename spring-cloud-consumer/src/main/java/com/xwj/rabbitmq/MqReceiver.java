package com.xwj.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费者
 */
@Slf4j
@Service
public class MqReceiver {

	/**
	 * 监听DIRECT_QUEUE队列
	 */
	@RabbitListener(queues = MqConsts.DIRECT_QUEUE)
	public void receive(String message) {
		log.info("receive message: " + message);
	}

	/**
	 * 监听相同的两个队列(跟上面一个一样)
	 * 
	 * 结果：可以看到是实现了轮询的方式对消息进行消费，而且不存在重复消费
	 */
	@RabbitListener(queues = MqConsts.DIRECT_QUEUE)
	public void receive2(String message) {
		log.info("receive message2: " + message);
	}

	/**
	 * 监听TOPIC_QUEUE1队列
	 */
	@RabbitListener(queues = MqConsts.TOPIC_QUEUE1)
	public void receiveTopic1(String message) {
		log.info("receive topic message1: " + message);
	}

	/**
	 * 监听TOPIC_QUEUE2队列
	 */
	@RabbitListener(queues = MqConsts.TOPIC_QUEUE2)
	public void receiveTopic2(String message) {
		log.info("receive topic message2: " + message);
	}

	/**
	 * 监听FANOUT_QUEUE1队列
	 */
	@RabbitListener(queues = MqConsts.FANOUT_QUEUE1)
	public void receiveFanout1(String message) {
		log.info("receive fanout message1: " + message);
	}

	/**
	 * 监听FANOUT_QUEUE2队列
	 */
	@RabbitListener(queues = MqConsts.FANOUT_QUEUE2)
	public void receiveFanout2(String message) {
		log.info("receive fanout message2: " + message);
	}

}
