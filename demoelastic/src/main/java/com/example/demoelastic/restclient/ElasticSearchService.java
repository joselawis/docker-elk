package com.example.demoelastic.restclient;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

@Slf4j
public class ElasticSearchService {

  private static final String elasticSearchUser = "elastic";
  private static final String elasticSearchPass = "changeme";
  private static final String elasticSearchHost = "localhost";
  private static final int elasticSearchPort = 9200;
  private final String clazz;
  private ElasticsearchClient client;
  private RestClient restClient;

  private ElasticSearchService(Class<?> clazz) {
    this.clazz = clazz.getName();
    initiateClient();
  }

  public static ElasticSearchService getElasticService(Class<?> clazz) {
    return new ElasticSearchService(clazz);
  }

  @PostConstruct
  private void initiateClient() {
    final CredentialsProvider credentialsProvider =
        new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(elasticSearchUser, elasticSearchPass));

    RestClientBuilder builder = RestClient.builder(
        new HttpHost(elasticSearchHost, elasticSearchPort))
        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
            .setDefaultCredentialsProvider(credentialsProvider));
    // Create the low-level client
    restClient = builder.build();

    // Create the transport with a Jackson mapper
    ElasticsearchTransport transport = new RestClientTransport(
        restClient, new JacksonJsonpMapper());

    // And create the API client
    client = new ElasticsearchClient(transport);
    log.info("Elasticsearch initiated");
  }

  @PreDestroy
  private void destroy() {
    try {
      restClient.close();
      log.info("Elasticsearch destroyed");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void logObject(String index, Object document) throws Exception {
    try {
      final BulkResponse response = client.bulk(builder -> {
        builder.index(index)
            .operations(ob -> {
              ob.index(ib -> ib.document(document));
              return ob;
            });

        return builder;
      });
    } catch (IOException e) {
      log.error("ERROR", e);
      throw new Exception(e);
    }
  }

  public void logMap(String index, Map<String, String> keyValue) throws Exception {
    keyValue.put("class", clazz);
    logObject(index, keyValue);
  }
}
