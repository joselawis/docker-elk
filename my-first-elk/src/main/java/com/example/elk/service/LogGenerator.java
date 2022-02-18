package com.example.elk.service;

import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogGenerator {

  public void generate(int count) {
    log.info("Start generating logs");
    LongStream.range(0, count)
        .forEach(i -> log.info("Log {}", i));
  }
}
