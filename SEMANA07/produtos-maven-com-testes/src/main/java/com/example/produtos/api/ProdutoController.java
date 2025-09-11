package com.example.produtos.api;

import com.example.produtos.api.dto.*;
import com.example.produtos.api.mapper.ProdutoMapper;
import com.example.produtos.domain.Produto;
import com.example.produtos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
  private final ProdutoService service;
  public ProdutoController(ProdutoService service) { this.service = service; }

  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public ProdutoResponse criar(@RequestBody @Valid ProdutoRequest req) {
    Produto salvo = service.criar(ProdutoMapper.toEntity(req));
    return ProdutoMapper.toResponse(salvo);
  }

  @GetMapping("/{id}")
  public ProdutoResponse porId(@PathVariable Long id) {
    return ProdutoMapper.toResponse(service.buscarPorId(id));
  }

  @PutMapping("/{id}")
  public ProdutoResponse atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest req) {
    Produto atualizado = service.atualizar(id, ProdutoMapper.toEntity(req));
    return ProdutoMapper.toResponse(atualizado);
  }

  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletar(@PathVariable Long id) { service.deletar(id); }

  @GetMapping
  public Page<ProdutoResponse> listar(@RequestParam(value="q", required=false) String q, Pageable pageable) {
    return service.listar(q, pageable).map(ProdutoMapper::toResponse);
  }
}
