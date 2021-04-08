package com.xwj.common;

import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;

public class DistributedTopic implements MessageListener<String> {

	@Override
	public void onMessage(Message<String> message) {
		System.out.println("DistributedTopic Got message-----------> " + message.getMessageObject());
	}

}