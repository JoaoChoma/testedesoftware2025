package com.teste.spring.teste;


import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.exception.*;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    ClienteRepository repo;

    @InjectMocks
    ClienteService service;

    @Test
    void criar_deveLancarSeEmailJaExiste() {
        Cliente c = new Cliente();
        c.setNome("João");
        c.setEmail("j@ex.com");
        when(repo.existsByEmail("j@ex.com")).thenReturn(true);

        assertThatThrownBy(() -> service.criar(c))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email já cadastrado");
        verify(repo, never()).save(any());
    }

    @Test
    void atualizar_deveAtualizarCamposBasicos() {
        Cliente antigo = new Cliente();
        antigo.setId(1L);
        antigo.setNome("Antigo");
        antigo.setEmail("a@ex.com");
        antigo.setTelefone("11");

        when(repo.findById(1L)).thenReturn(Optional.of(antigo));
        when(repo.findByEmail("novo@ex.com")).thenReturn(Optional.of(antigo)); // mesmo cliente
        when(repo.existsByEmail("novo@ex.com")).thenReturn(true);
        when(repo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente dados = new Cliente();
        dados.setNome("Novo");
        dados.setEmail("novo@ex.com");
        dados.setTelefone("22");

        Cliente atualizado = service.atualizar(1L, dados);

        assertThat(atualizado.getNome()).isEqualTo("Novo");
        assertThat(atualizado.getEmail()).isEqualTo("novo@ex.com");
        assertThat(atualizado.getTelefone()).isEqualTo("22");
    }
}
