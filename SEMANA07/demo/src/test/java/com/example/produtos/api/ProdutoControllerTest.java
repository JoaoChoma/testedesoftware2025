package com.example.produtos.api;

import com.example.produtos.domain.Produto;
import com.example.produtos.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProdutoController.class)
class ProdutoControllerTest {

  @Autowired MockMvc mvc;
  @MockBean ProdutoService service;

  @Test
  void getPorId() throws Exception {
    when(service.buscarPorId(1L)).thenReturn(new Produto(1L, "Teclado", new BigDecimal("199.90")));

    mvc.perform(get("/api/produtos/1"))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.id").value(1))
       .andExpect(jsonPath("$.nome").value("Teclado"))
       .andExpect(jsonPath("$.valor").value(199.90));
  }

  @Test
  void criaValido() throws Exception {
    when(service.criar(any())).thenReturn(new Produto(5L, "Mouse", new BigDecimal("150.00")));

    mvc.perform(post("/api/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{"nome":"Mouse","valor":150.00}"))
       .andExpect(status().isCreated())
       .andExpect(jsonPath("$.id").value(5));
  }
}
