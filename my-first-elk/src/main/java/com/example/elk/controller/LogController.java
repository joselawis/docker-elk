package com.example.elk.controller;

import com.example.elk.service.LogGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LogController {
  @Autowired
  private LogGenerator generator;

  @GetMapping("/generate")
  public ResponseEntity test(@RequestParam(name = "count", defaultValue = "0") Integer count) {
    log.info("Test request received with count: {}", count);
    generator.generate(count);
    return ResponseEntity.ok("Success!");
  }
}