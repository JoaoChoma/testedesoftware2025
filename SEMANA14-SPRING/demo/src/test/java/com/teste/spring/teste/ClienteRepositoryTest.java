package com.teste.spring.teste;


import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    ClienteRepository repo;

    @Test
    void deveSalvarEConsultarPorEmail() {
        Cliente c = new Cliente();
        c.setNome("Ana");
        c.setEmail("ana@ex.com");
        c.setTelefone("1199999-0000");
        repo.save(c);

        assertThat(repo.existsByEmail("ana@ex.com")).isTrue();
        assertThat(repo.findByEmail("ana@ex.com")).isPresent();
    }
}
