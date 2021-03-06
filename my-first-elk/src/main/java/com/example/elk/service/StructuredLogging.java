package com.example.elk.service;

import static net.logstash.logback.argument.StructuredArguments.keyValue;
import static net.logstash.logback.argument.StructuredArguments.kv;
import static net.logstash.logback.argument.StructuredArguments.v;
import static net.logstash.logback.argument.StructuredArguments.value;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class StructuredLogging {

  void simpleLog() {
    log.info("SIMPLE LOG - Order {} saved", 123);
  }

  void simpleLog2() {
    log.info("SIMPLE LOG 2 - Order saved orderId", 123);
  }

  void logValue() {
    String orderId = "123";
    log.info("LOG VALUE - Order {} saved", value("orderId", orderId));
  }

  void logMdc() {
    String orderId = "123";
    try (MDCCloseable ignored = MDC.putCloseable("orderId", orderId)) {
      log.info("LOG MDC - Order saved");
    }
  }

  void logMdc2() {
    String orderId = "123";
    MDC.put("orderId", orderId);
    log.info("LOG MDC 2 - Order saved");
    MDC.remove("orderId");
  }

  void logValue2() {
    String orderId = "123";
    log.info("LOG VALUE 2 - Order saved {}", v("orderId", orderId));
  }

  void logValue3() {
    String oldStatus = "NEW";
    String newStatus = "READY";
    log.info("LOG VALUE 3 - Status changed {}->{}.", v("oldStatus", oldStatus),
        v("newStatus", newStatus));
  }

  void logKeyValue() {
    String orderId = "123";
    log.info("LOG KEY VALUE - Order saved {}", keyValue("orderId", orderId));
  }

  void logKeyValue2() {
    String orderId = "123";
    String status = "NEW";
    log.info("LOG KEY VALUE 2 - Order saved", kv("orderId", orderId), kv("status", status));
  }

  void logObject() {
    Order order = new Order("123", "NEW", null);
    log.info("LOG OBJECT - Order saved", kv("order", order));
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
