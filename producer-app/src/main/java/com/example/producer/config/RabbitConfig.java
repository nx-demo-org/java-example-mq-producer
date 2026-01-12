package com.example.producer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  public static final String EXCHANGE = "orders.exchange";
  public static final String QUEUE = "orders.v1.queue";
  public static final String ROUTING_KEY = "orders.v1";

  @Bean
  public DirectExchange ordersExchange() {
    return new DirectExchange(EXCHANGE, true, false);
  }

  @Bean
  public Queue ordersQueue() {
    return new Queue(QUEUE, true);
  }

  @Bean
  public Binding ordersBinding(Queue ordersQueue, DirectExchange ordersExchange) {
    return BindingBuilder.bind(ordersQueue).to(ordersExchange).with(ROUTING_KEY);
  }
}
