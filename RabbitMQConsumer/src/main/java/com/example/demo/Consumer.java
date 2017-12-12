package com.example.demo;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

	
	@RabbitListener(queues="${sample.rabbitmq.queue}")
	public void recieveMessage(String message) {
		
		System.out.println("Message Recieved.."+message);
	}
}
