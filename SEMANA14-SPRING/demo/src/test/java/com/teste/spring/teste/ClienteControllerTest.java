package com.teste.spring.teste;


import com.teste.spring.teste.controller.ClienteController;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
public class ClienteControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ClienteRepository repo;

    @MockBean
    ClienteService service; // injetado no controller, mas não usado aqui

    @Test
    void listar_deveRetornarPaginaDeClientes() throws Exception {
        // 1️⃣ Simular dados retornados pelo repositório
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("Ana");
        cliente1.setEmail("ana@ex.com");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Bruno");
        cliente2.setEmail("bruno@ex.com");

        Page<Cliente> paginaSimulada = new PageImpl<>(
                List.of(cliente1, cliente2),
                PageRequest.of(0, 10, Sort.by("id").ascending()),
                2
        );

        // 2️⃣ Definir comportamento do mock
        when(repo.findAll(any(Pageable.class))).thenReturn(paginaSimulada);

        // 3️⃣ Executar requisição GET simulada
        mvc.perform(get("/api/clientes")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .accept(MediaType.APPLICATION_JSON))
                // 4️⃣ Verificar status HTTP e estrutura JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].nome").value("Ana"))
                .andExpect(jsonPath("$.content[1].email").value("bruno@ex.com"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }
}
