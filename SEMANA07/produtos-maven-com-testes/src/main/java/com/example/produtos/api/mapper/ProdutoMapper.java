package com.example.produtos.api.mapper;

import com.example.produtos.api.dto.*;
import com.example.produtos.domain.Produto;

public class ProdutoMapper {
  public static Produto toEntity(ProdutoRequest req) {
    return new Produto(null, req.nome(), req.valor());
  }
  public static void copyToEntity(ProdutoRequest req, Produto entity) {
    entity.setNome(req.nome());
    entity.setValor(req.valor());
  }
  public static ProdutoResponse toResponse(Produto p) {
    return new ProdutoResponse(p.getId(), p.getNome(), p.getValor());
  }
}
