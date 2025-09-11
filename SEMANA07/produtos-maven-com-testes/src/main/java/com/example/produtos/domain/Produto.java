package com.example.produtos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank @Size(max = 120)
  private String nome;
  @DecimalMin("0.00")
  private BigDecimal valor;

  public Produto() {}
  public Produto(Long id, String nome, BigDecimal valor) {
    this.id = id; this.nome = nome; this.valor = valor;
  }
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getNome() { return nome; }
  public void setNome(String nome) { this.nome = nome; }
  public BigDecimal getValor() { return valor; }
  public void setValor(BigDecimal valor) { this.valor = valor; }
}
