package com.example.producer.web;

import com.example.contracts.avro.OrderCreatedEvent;
import com.example.producer.service.OrderPublisher;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RestController
public class ProducerController {

  private final OrderPublisher publisher;

  public ProducerController(OrderPublisher publisher) {
    this.publisher = publisher;
  }

  public record SendRequest(String orderId, String customerId) {}

  @PostMapping("/send")
  public Map<String, Object> send(@RequestBody SendRequest req) {
    var evt = OrderCreatedEvent.newBuilder()
        .setEventId(UUID.randomUUID().toString())
        .setOrderId(req.orderId())
        .setCustomerId(req.customerId())
        .setCreatedAt(Instant.now())
        .build();

    publisher.publish(evt);

    return Map.of(
        "ok", true,
        "sentCount", publisher.sentCount(),
        "eventId", evt.getEventId().toString()
    );
  }

  @GetMapping("/status")
  public Map<String, Object> status() {
    return Map.of("sentCount", publisher.sentCount());
  }
}
