package com.example.demo;



import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * @author jai.balaji.sukumar
 *
 */
@Profile("rmq")

@EnableRabbit
public class Configuration {

	@Value("${sample.rabbitmq.exchange}")
	private String EXCHANGE_NAME;

	@Value("${sample.rabbitmq.queue}")
	private String SAMPLE_QUEUE;

	/*@Value("${cancel.order.queue}")
	private String CANCEL_ORDER_QUEUE;*/

	@Value("${sample.rabbitmq.routingkey}")
	private String sampleRoutingKey;

/*	@Value("${cancel.order.routingKey}")
	private String cancelOrderRoutingKey;

	@Value("${confirm.order.response.queue}")
	private String CONFORM_ORDER_RESPONSE_QUEUE;

	@Value("${confirm.order.response.routingKey}")
	private String confirmOrderResponseRoutingKey;*/

	
/*
	@Bean
	Queue confirmOrderQueue() {
		return new Queue(CONFIRM_ORDER_QUEUE, true);
	}

	@Bean
	Queue cancelOrderQueue() {
		return new Queue(CANCEL_ORDER_QUEUE, true);
	}*/

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	@Bean
	Binding sampleBinding(DirectExchange exchange) {
		return BindingBuilder.bind(sampleBinding()).to(exchange).with(sampleRoutingKey);
	}

	@Bean
	Binding cancelOrderBinding(DirectExchange exchange) {
		return BindingBuilder.bind(cancelOrderQueue()).to(exchange).with(cancelOrderRoutingKey);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(CONFIRM_ORDER_QUEUE);
		container.setMessageListener(listenerAdapter);
		container.setPrefetchCount(1);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(InventoryRabbitListener receiver) {
		MessageListenerAdapter messageListener = new MessageListenerAdapter(receiver);
		messageListener.setDefaultListenerMethod("orderPlaced");
		return messageListener;
	}

	@Bean
	MessageListenerAdapter cancelledListenerAdapter(InventoryRabbitListener receiver) {
		MessageListenerAdapter messageListener = new MessageListenerAdapter(receiver);
		messageListener.setDefaultListenerMethod("orderCancelled");
		return messageListener;
	}

	@Bean
	SimpleMessageListenerContainer cancelledContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter cancelledListenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(CANCEL_ORDER_QUEUE);
		container.setMessageListener(cancelledListenerAdapter);
		container.setPrefetchCount(1);
		return container;
	}

	@Bean
	public Queue confirmOrderResponseQueue() {
		return new Queue(CONFORM_ORDER_RESPONSE_QUEUE);
	}

	@Bean
	public Binding confirmOrderResponseQueueBinding() {
		return BindingBuilder.bind(confirmOrderResponseQueue()).to(exchange()).with(confirmOrderResponseRoutingKey);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setQueue(CONFORM_ORDER_RESPONSE_QUEUE);
		return rabbitTemplate;
	}

}
