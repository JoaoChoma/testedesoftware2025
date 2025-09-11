package com.example.produtos.api.dto;

import java.math.BigDecimal;

public record ProdutoResponse(Long id, String nome, BigDecimal valor) {}
