package com.teste.spring.teste.service;


import com.teste.spring.teste.exception.BusinessException;
import com.teste.spring.teste.exception.NotFoundException;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Cliente criar(Cliente c) {
        if (repo.existsByEmail(c.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }
        return repo.save(c);
    }

    @Transactional(readOnly = true)
    public Cliente buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente dados) {
        Cliente existente = buscar(id);
        existente.setNome(dados.getNome());
        existente.setEmail(dados.getEmail());
        existente.setTelefone(dados.getTelefone());
        if (repo.existsByEmail(existente.getEmail()) &&
                repo.findByEmail(existente.getEmail()).map(e -> !e.getId().equals(id)).orElse(false)) {
            throw new BusinessException("Email já cadastrado para outro cliente");
        }
        return repo.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente existente = buscar(id);
        repo.delete(existente);
    }
}
