package org.example.checkout;

import java.util.Objects;

public class Item {
    private final String categoria;
    private final double precoUnitario;
    private final int quantidade;

    public Item(String categoria, double precoUnitario, int quantidade) {
        if (precoUnitario < 0) throw new IllegalArgumentException("precoUnitario < 0");
        if (quantidade <= 0) throw new IllegalArgumentException("quantidade <= 0");
        this.categoria = Objects.requireNonNull(categoria, "categoria");
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
    }

    public String getCategoria() { return categoria; }
    public double getPrecoUnitario() { return precoUnitario; }
    public int getQuantidade() { return quantidade; }

    public double subtotal() {
        return precoUnitario * quantidade;
    }
}
