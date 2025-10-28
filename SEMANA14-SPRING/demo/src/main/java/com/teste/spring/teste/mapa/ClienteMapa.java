package com.teste.spring.teste.mapa;


import com.teste.spring.teste.dto.ClienteDto;
import com.teste.spring.teste.model.Cliente;

public class ClienteMapa {
    public static ClienteDto toDTO(Cliente e) {
        ClienteDto d = new ClienteDto();
        d.setId(e.getId());
        d.setNome(e.getNome());
        d.setEmail(e.getEmail());
        d.setTelefone(e.getTelefone());
        return d;
    }
    public static Cliente toEntity(ClienteDto d) {
        Cliente e = new Cliente();
        e.setId(d.getId());
        e.setNome(d.getNome());
        e.setEmail(d.getEmail());
        e.setTelefone(d.getTelefone());
        return e;
    }
    public static void copyToEntity(ClienteDto d, Cliente e) {
        e.setNome(d.getNome());
        e.setEmail(d.getEmail());
        e.setTelefone(d.getTelefone());
    }
}
