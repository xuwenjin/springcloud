package com.xwj.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 **/
@Configuration
public class RabbitConfig {

	@Bean
	public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		// 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
		rabbitTemplate.setMandatory(true);

		/**
		 * 触发该回调情况：
		 * 
		 * 1、找不到交换机，ack=false
		 * 
		 * 2、找到交换机，但是交换机上没有绑定队列，ack=true
		 * 
		 * 3、找不到交换机和队列(和1情况一样)，ack=false
		 * 
		 * 4、正常发送，ack=true
		 */
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				System.out.println("ConfirmCallback:     " + "相关数据：" + correlationData);
				System.out.println("ConfirmCallback:     " + "确认情况：" + ack);
				System.out.println("ConfirmCallback:     " + "原因：" + cause);
			}
		});

		/**
		 * 触发该回调情况：找到交换机，但是交换机上没有绑定队列
		 */
		rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
				System.out.println("ReturnCallback:     " + "消息：" + message);
				System.out.println("ReturnCallback:     " + "回应码：" + replyCode);
				System.out.println("ReturnCallback:     " + "回应信息：" + replyText);
				System.out.println("ReturnCallback:     " + "交换机：" + exchange);
				System.out.println("ReturnCallback:     " + "路由键：" + routingKey);
			}
		});

		return rabbitTemplate;
	}

}