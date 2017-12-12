package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdController {

	
	@Autowired
	Producer producer;
	
	@RequestMapping("/sendMessage")
	public String sendMsg(@RequestParam("message")String message){
		producer.produceMsg(message);
		return "Message Sent";
	}
	
}
