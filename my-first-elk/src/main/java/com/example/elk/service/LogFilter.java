package com.example.elk.service;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {

  private static final String REQUEST_ID = "requestId";

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String requestId = request.getHeader(REQUEST_ID);
    if (requestId == null) {
      requestId = UUID.randomUUID().toString();
    }

    MDC.put(REQUEST_ID, requestId);

    try {
      log.info("Started process request with {} : {}", REQUEST_ID, requestId);
      filterChain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
