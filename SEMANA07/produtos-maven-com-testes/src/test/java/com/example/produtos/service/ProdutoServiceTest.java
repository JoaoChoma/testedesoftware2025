package com.example.produtos.service;

import com.example.produtos.domain.Produto;
import com.example.produtos.repo.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

  ProdutoRepository repo = mock(ProdutoRepository.class);
  ProdutoService service = new ProdutoService(repo);

  @Test
  void criaProduto() {
    var p = new Produto(null, "Teclado", new BigDecimal("199.90"));
    when(repo.save(any())).thenAnswer(inv -> {
      Produto in = inv.getArgument(0);
      in.setId(1L);
      return in;
    });

    var salvo = service.criar(p);

    assertThat(salvo.getId()).isEqualTo(1L);
    var captor = ArgumentCaptor.forClass(Produto.class);
    verify(repo).save(captor.capture());
    assertThat(captor.getValue().getNome()).isEqualTo("Teclado");
  }

  @Test
  void buscaOu404() {
    when(repo.findById(99L)).thenReturn(Optional.empty());
    assertThatThrownBy(() -> service.buscarPorId(99L))
        .isInstanceOf(NotFoundException.class);
  }
}
