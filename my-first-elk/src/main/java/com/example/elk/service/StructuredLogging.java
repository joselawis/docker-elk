package com.example.elk.service;

import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static net.logstash.logback.argument.StructuredArguments.kv;
import static net.logstash.logback.argument.StructuredArguments.v;
import static net.logstash.logback.argument.StructuredArguments.value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.stereotype.Service;

@Service
class StructuredLogging {

  private static final Logger log = LoggerFactory.getLogger("MyApplication");

  void simpleLog() {
    log.info("Order {} saved", 123);
  }

  void simpleLog2() {
    log.info("Order saved orderId", 123);
  }

  void logValue() {
    String orderId = "123";
    log.info("Order {} saved", value("orderId", orderId));
  }

  void logMdc() {
    String orderId = "123";
    try (MDCCloseable ignored = MDC.putCloseable("orderId", orderId)) {
      log.info("Order saved");
    }
  }

  void logMdc2() {
    String orderId = "123";
    MDC.put("orderId", orderId);
    log.info("Order saved");
    MDC.remove("orderId");
  }

  void logValue2() {
    String orderId = "123";
    log.info("Order saved {}", v("orderId", orderId));
  }

  void logValue3() {
    String oldStatus = "NEW";
    String newStatus = "READY";
    log.info("Status changed {}->{}.", v("oldStatus", oldStatus), v("newStatus", newStatus));
  }

  void logKeyValue() {
    String orderId = "123";
    log.info("Order saved {}", keyValue("orderId", orderId));
  }

  void logKeyValue2() {
    String orderId = "123";
    String status = "NEW";
    log.info("Order saved", kv("orderId", orderId), kv("status", status));
  }

  void logObject() {
    Order order = new Order("123", "NEW", null);
    log.info("Order saved", kv("order", order));
  }

  static class Order {

    String orderId;
    String status;
    String canceled;

    Order(String orderId, String status, String canceled) {
      this.orderId = orderId;
      this.status = status;
      this.canceled = canceled;
    }

    public String getOrderId() {
      return orderId;
    }

    public String getStatus() {
      return status;
    }

    public String getCanceled() {
      return canceled;
    }
  }
}
