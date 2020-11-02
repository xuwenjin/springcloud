package com.xwj.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xwj.rabbitmq.MqConsts;

/**
 * topic模式(模糊匹配模式)：
 * 
 * 模糊匹配routingKey来使交换机与哪个队列绑定。匹配交换器的匹配符 （星号）表示一个单词 #（井号）表示零个或者多个单词
 * 
 * 如果消费者端的路由关键字只使用【#】来匹配消息，在匹配【topic】模式下，它会变成一个分发【fanout】模式，接收所有消息。
 * 
 * 如果消费者端的路由关键字中没有【#】或者【*】，它就变成直连【direct】模式来工作。
 * 
 * 特殊例子：
 * 
 * 消费者端路由关键字 "*", 不能接收到生产者端发来路由关键字为空的消息
 * 
 * 消费者端路由关键字 "#", 能接收到生产者端发来路由关键字为空的消息
 * 
 * 消费者端路由关键字"#.*", 能接收到生产者端以“..”为关键字的消息, 如果发送来的消息只有一个单词，不能匹配上
 */
@Configuration
public class TopicRabbitConfig {

	/**
	 * 创建一个队列
	 */
	@Bean
	public Queue topicQueue1() {
		return new Queue(MqConsts.TOPIC_QUEUE1, true);
	}

	/**
	 * 创建一个队列
	 */
	@Bean
	public Queue topicQueue2() {
		return new Queue(MqConsts.TOPIC_QUEUE2, true);
	}

	/**
	 * 创建一个topic的交换机
	 */
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange(MqConsts.TOPIC_EXCHANGE);
	}

	/**
	 * 将topic1以路由键为"topic.key1"绑定到交换机上
	 */
	@Bean
	public Binding topicBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
	}

	/**
	 * 将topic2以路由键为"topic.#"绑定到交换机上
	 */
	@Bean
	public Binding topicBinding2() {
		return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
	}

}
