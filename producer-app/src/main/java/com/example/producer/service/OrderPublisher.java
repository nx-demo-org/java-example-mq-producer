package com.example.producer.service;

import com.example.producer.avro.AvroCodec;
import com.example.producer.config.RabbitConfig;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final AtomicLong sent = new AtomicLong();

  public OrderPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public long sentCount() {
    return sent.get();
  }

  public void publish(SpecificRecord event) {
    byte[] body = AvroCodec.encode(event);

    var msg = MessageBuilder
        .withBody(body)
        .setContentType("avro/binary")
        .build();

    rabbitTemplate.send(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, msg);
    sent.incrementAndGet();
  }
}
