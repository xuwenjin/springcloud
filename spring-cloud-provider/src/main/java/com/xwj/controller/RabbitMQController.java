package com.xwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xwj.rabbitmq.MqSender;

@RestController
@RequestMapping("rabbitmq")
public class RabbitMQController {

	@Autowired(required = false)
	private MqSender mqSender;

	/**
	 * 发送消息
	 */
	@GetMapping("/sendMsg")
	public String sendMsg() {
		mqSender.sendDirectMessage();
		// mqSender.sendTopicMessage();
		// mqSender.sendFanoutMessage();
		// mqSender.sendException();

		return "OK";
	}

}
