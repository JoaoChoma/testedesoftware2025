package com.example.produtos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdutoIntegrationTest {

  @LocalServerPort int port;
  @Autowired TestRestTemplate rest;

  @Test
  void fluxoCrudSimples() {
    var body = "{"nome":"Fone","valor":99.99}";
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    var entity = new HttpEntity<>(body, headers);

    var respPost = rest.postForEntity(url("/api/produtos"), entity, String.class);
    assertThat(respPost.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    var respList = rest.getForEntity(url("/api/produtos"), String.class);
    assertThat(respList.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(respList.getBody()).contains("Fone");
  }

  private String url(String path) { return "http://localhost:" + port + path; }
}
