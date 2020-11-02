package com.xwj.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xwj.rabbitmq.MqConsts;

/**
 * direct模式(直连模式)： 通过完全匹配routingKey来使交换机与哪个队列绑定(一个交换机绑定队列时，可以有多个routingKey)
 *
 */
@Configuration
public class DirectRabbitConfig {

	/**
	 * 创建一个队列
	 */
	@Bean
	public Queue directQueue() {
		/*
		 * name：队列名称
		 * durable：是否持久化。默认true，持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
		 * exclusive：默认false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
		 * autoDelete：是否自动删除。默认false，当没有生产者或者消费者使用此队列，该队列会自动删除
		 */
		return new Queue(MqConsts.DIRECT_QUEUE, true);
	}

	/**
	 * Direct交换机
	 */
	@Bean
	public DirectExchange xwjDirectExchange() {
		// return new DirectExchange("TestDirectExchange",true,true);
		return new DirectExchange(MqConsts.DIRECT_EXCHANGE, true, false);
	}

	/**
	 * 将队列和交换机绑定。并设置用于匹配键：TestDirectRouting
	 */
	@Bean
	public Binding bindingDirect() {
		return BindingBuilder.bind(directQueue()).to(xwjDirectExchange()).with(MqConsts.DIRECT_ROUTINGKEY);
	}

}
