package com.xwj.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xwj.rabbitmq.MqConsts;

/**
 * fanout模式(广播模式)： 会将消息发送给所有队列 fanout模式，消费者将队列跟交换器进行绑定时，可以不用指定具体的routingKey
 */
@Configuration
public class FanoutRabbitConfig {

	/**
	 * 创建一个队列
	 */
	@Bean
	public Queue fanoutQueue1() {
		return new Queue(MqConsts.FANOUT_QUEUE1, true);
	}

	/**
	 * 创建一个队列
	 */
	@Bean
	public Queue fanoutQueue2() {
		return new Queue(MqConsts.FANOUT_QUEUE2, true);
	}

	/**
	 * 创建一个fanout的交换机
	 */
	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange(MqConsts.FUNOUT_EXCHANGE);
	}

	/**
	 * 将fanout1绑定到交换机上(广播模式，不需要routingKey)
	 */
	@Bean
	public Binding fanoutBinding1() {
		return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
	}

	/**
	 * 将fanout2绑定到交换机上(广播模式，不需要routingKey)
	 */
	@Bean
	public Binding fanoutBinding2() {
		return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
	}

}
