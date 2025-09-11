package com.example.produtos.repo;

import com.example.produtos.domain.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProdutoRepositoryTest {

  @Autowired ProdutoRepository repo;

  @Test
  void buscaPorNome() {
    repo.save(new Produto(null, "Mouse Gamer", new BigDecimal("250.00")));
    repo.save(new Produto(null, "Cadeira", new BigDecimal("800.00")));

    var page = repo.findByNomeContainingIgnoreCase("mouse", PageRequest.of(0, 10));

    assertThat(page.getTotalElements()).isEqualTo(1);
    assertThat(page.getContent().get(0).getNome()).containsIgnoringCase("mouse");
  }
}
