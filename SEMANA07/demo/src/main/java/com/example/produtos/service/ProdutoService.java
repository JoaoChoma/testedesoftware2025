package com.example.produtos.service;

import com.example.produtos.domain.Produto;
import com.example.produtos.repo.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {
  private final ProdutoRepository repo;
  public ProdutoService(ProdutoRepository repo) { this.repo = repo; }

  @Transactional
  public Produto criar(Produto p) { return repo.save(p); }

  @Transactional(readOnly = true)
  public Produto buscarPorId(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Produto " + id + " não encontrado"));
  }

  @Transactional
  public Produto atualizar(Long id, Produto novo) {
    Produto existente = buscarPorId(id);
    existente.setNome(novo.getNome());
    existente.setValor(novo.getValor());
    return repo.save(existente);
  }

  @Transactional
  public void deletar(Long id) {
    repo.delete(buscarPorId(id));
  }

  @Transactional(readOnly = true)
  public Page<Produto> listar(String q, Pageable pageable) {
    if (q == null || q.isBlank()) return repo.findAll(pageable);
    return repo.findByNomeContainingIgnoreCase(q, pageable);
  }
}
