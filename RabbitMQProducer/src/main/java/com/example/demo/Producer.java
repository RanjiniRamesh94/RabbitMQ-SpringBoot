package com.example.demo;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Producer {

	@Autowired
	AmqpTemplate amqpTemplate;

	@Value("${sample.rabbitmq.exchange}")
	private String exchange;

	@Value("${sample.rabbitmq.routingkey}")
	private String routingkey;

	public void produceMsg(String msg) {
		
		amqpTemplate.convertAndSend(exchange, routingkey, msg);
		
		System.out.println("Send msg = " + msg);
	}

}
