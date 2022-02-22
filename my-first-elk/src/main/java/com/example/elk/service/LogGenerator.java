package com.example.elk.service;

import static net.logstash.logback.argument.StructuredArguments.v;

import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogGenerator {

  @Autowired
  StructuredLogging structuredLogging;

  public void generate(int count) {

    log.info("Start generating logs");
    LongStream.range(0, count)
        .forEach(i -> log.info("Log {}", v("orderId", i)));

    structuredLogging.simpleLog();
    structuredLogging.simpleLog2();
    structuredLogging.logValue();
    structuredLogging.logMdc();
    structuredLogging.logMdc2();
    structuredLogging.logValue2();
    structuredLogging.logValue3();
    structuredLogging.logKeyValue();
    structuredLogging.logKeyValue2();
    structuredLogging.logObject();

    log.debug("UN DEBUG - {}", v("debugAttribute", 123456789));

    try {
      int v = 1 / 0;
    } catch (Exception e) {
      log.error(
          "ERROR VALUES - {} - {}", v("exceptionName", e.getClass().getName()),
          v("exceptionMessage",
              e.getMessage()), e);
      log.error("ERROR BASIC - Hola ", e);
    }
  }
}
