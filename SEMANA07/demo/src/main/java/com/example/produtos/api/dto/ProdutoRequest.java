package com.example.produtos.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequest(@NotBlank @Size(max=120) String nome,
                             @DecimalMin("0.00") BigDecimal valor) {}
