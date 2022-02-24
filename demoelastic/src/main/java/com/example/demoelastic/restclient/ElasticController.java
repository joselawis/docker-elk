package com.example.demoelastic.restclient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.indices.Alias;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ElasticController {

  private static final String elasticSearchUser = "elastic";
  private static final String elasticSearchPass = "changeme";
  private static final String elasticSearchHost = "localhost";
  private static final int elasticSearchPort = 9200;

  private static final String INDEX = "my_index";
  private ElasticsearchClient client;

  @GetMapping("/generate")
  public ResponseEntity test() {
    try {
      final CredentialsProvider credentialsProvider =
          new BasicCredentialsProvider();
      credentialsProvider.setCredentials(AuthScope.ANY,
          new UsernamePasswordCredentials(elasticSearchUser, elasticSearchPass));

      RestClientBuilder builder = RestClient.builder(
          new HttpHost(elasticSearchHost, elasticSearchPort))
          .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
              .setDefaultCredentialsProvider(credentialsProvider));
      // Create the low-level client
      RestClient restClient = builder.build();

      // Create the transport with a Jackson mapper
      ElasticsearchTransport transport = new RestClientTransport(
          restClient, new JacksonJsonpMapper());

      // And create the API client
      client = new ElasticsearchClient(transport);

      save(Product.builder().id("HOLA").name("CACA RODRIGUEZ SEGUNDO").description("ME LLAMO CACA")
          .build());

      restClient.close();
      return ResponseEntity.ok("Success!");
    } catch (IOException e) {
      log.error("ERROR", e);
      return ResponseEntity.ok("ERROR");
    }

  }

  private void createIndex(String indexName) {
    // Create the "products" index
    try {
      client.indices().create(c -> c.index(indexName));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save(Product product) throws IOException {
    save(Collections.singletonList(product));
  }

  public void save(List<Product> products) throws IOException {
    final BulkResponse response = client.bulk(builder -> {
      for (Product product : products) {
        builder.index(INDEX)
            .operations(ob -> {
              if (product.getId() != null) {
                ob.index(ib -> ib.document(product).id(product.getId()));
              } else {
                ob.index(ib -> ib.document(product));
              }
              return ob;
            });
      }
      return builder;
    });

    final int size = products.size();
    for (int i = 0; i < size; i++) {
      products.get(i).setId(response.items().get(i).id());
    }
  }


  private void createWithBuilder(String indexName, String alias) {
    try {
      CreateIndexResponse createResponse = client.indices().create(
          new CreateIndexRequest.Builder()
              .index(indexName)
              .aliases(alias,
                  new Alias.Builder().isWriteIndex(true).build()
              )
              .build()
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void createWithBuilder2(String indexName, String alias) {
    try {
      CreateIndexResponse createResponse = client.indices()
          .create(c -> c
              .index(indexName)
              .aliases(alias, a -> a
                  .isWriteIndex(true)
              )
          );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String randomId() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

}
