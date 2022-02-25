package com.example.demoelastic.restclient;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ElasticController {

  private static final String INDEX = "my_index";

  private final ElasticSearchService elasticSearchService = ElasticSearchService
      .getElasticService(ElasticController.class);

  @GetMapping("/generate")
  public ResponseEntity test() {
    try {
      elasticSearchService.logObject(INDEX,
          Product.builder()
              .id(randomId())
              .name("Juanito")
              .description("Soy un tio normal")
              .build());
      elasticSearchService.logMap(INDEX, new HashMap<String, String>() {{
            put("name", "Benito");
            put("description", "Me fumo 4 porros");
          }}
      );
      return ResponseEntity.ok("Success!");
    } catch (Exception e) {
      log.error("ERROR", e);
      return ResponseEntity.of(Optional.of("ERROR"));
    }
  }

  private String randomId() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

}
