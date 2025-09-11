package com.example.demo;

import com.example.demo.controller.UserRegister;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Carrega o contexto web apenas para o UserRegister, tornando o teste mais leve
@WebMvcTest(UserRegister.class)
class UserTest {

    // Ferramenta principal para simular requisições HTTP (GET, POST, etc.)
    @Autowired
    private MockMvc mockMvc;

    // Cria um "dublê" do UserService. Podemos controlar seu comportamento.
    @MockBean
    private UserService userService;

    // Utilitário para converter objetos Java em JSON e vice-versa
    @Autowired
    private ObjectMapper objectMapper;

    UserTest(UserService userService) {
        this.userService = userService;
    }

    /**
     * Teste do "caminho feliz": o usuário é registrado com sucesso.
     */
    @Test
    void quandoRegistrarUsuario_comDadosValidos_deveRetornarCreated() throws Exception {
        // 1. Arrange (Preparação)
        UserDto userDto = new UserDto();
        userDto.setUsername("novoUsuario");
        userDto.setPassword("senhaSegura123");

        // Configura o mock: quando `registerNewUser` for chamado com qualquer UserDto,
        // não faça nada (simulando um registro bem-sucedido sem erro).
        doNothing().when(userService).registerNewUser(any(UserDto.class));

        // 2. Act & 3. Assert (Ação e Verificação)
        mockMvc.perform(post("/register") // Faz um POST para /register
                        .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                        .content(objectMapper.writeValueAsString(userDto))) // Converte o DTO em uma string JSON
                .andExpect(status().isCreated()) // Espera que o status HTTP seja 201 CREATED
                .andExpect(content().string("Usuário registrado com sucesso!")); // Espera que a mensagem no corpo da resposta seja essa
    }

    /**
     * Teste do cenário de conflito: o nome de usuário já existe.
     */
    @Test
    void quandoRegistrarUsuario_comUsernameExistente_deveRetornarConflict() throws Exception {
        // 1. Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("usuarioExistente");
        userDto.setPassword("senha123");

        String mensagemDeErro = "Erro: Nome de usuário já está em uso.";

        // Configura o mock: quando `registerNewUser` for chamado, lance uma exceção
        // `IllegalStateException`, que é o que o controller espera para o caso de conflito.
        doThrow(new IllegalStateException(mensagemDeErro)).when(userService).registerNewUser(any(UserDto.class));

        // 2. Act & 3. Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict()) // Espera que o status HTTP seja 409 CONFLICT
                .andExpect(content().string(mensagemDeErro)); // Espera que a mensagem de erro seja retornada
    }

    /**
     * Teste de um erro genérico no servidor.
     */
    @Test
    void quandoRegistrarUsuario_eOcorrerErroInesperado_deveRetornarInternalServerError() throws Exception {
        // 1. Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("outroUsuario");
        userDto.setPassword("senha123");

        // Configura o mock: simula uma falha inesperada no serviço (ex: falha de conexão com o banco)
        doThrow(new RuntimeException()).when(userService).registerNewUser(any(UserDto.class));

        // 2. Act & 3. Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isInternalServerError()) // Espera que o status HTTP seja 500 INTERNAL_SERVER_ERROR
                .andExpect(content().string("Ocorreu um erro inesperado.")); // Espera a mensagem genérica de erro
    }
}