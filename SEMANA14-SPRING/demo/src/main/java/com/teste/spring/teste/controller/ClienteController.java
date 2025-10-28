package com.teste.spring.teste.controller;

import com.teste.spring.teste.dto.ClienteDto;
import com.teste.spring.teste.repository.ClienteRepository;
import com.teste.spring.teste.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.teste.spring.teste.model.Cliente;
import com.teste.spring.teste.mapa.ClienteMapa;
import com.teste.spring.teste.exception.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService service;
    private final ClienteRepository repo;

    public ClienteController(ClienteService service, ClienteRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<ClienteDto> criar(@Valid @RequestBody ClienteDto dto) {
        Cliente salvo = service.criar(ClienteMapa.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapa.toDTO(salvo));
    }

    @GetMapping("/{id}")
    public ClienteDto buscar(@PathVariable Long id) {
        return ClienteMapa.toDTO(service.buscar(id));
    }

    @GetMapping
    public Page<ClienteDto> listar(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id,asc") String sort) {
        String[] s = sort.split(",");
        Pageable p = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(s[1]), s[0]));
        return repo.findAll(p).map(ClienteMapa::toDTO);
    }

    @PutMapping("/{id}")
    public ClienteDto atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto dto) {
        Cliente atualizado = service.atualizar(id, ClienteMapa.toEntity(dto));
        return ClienteMapa.toDTO(atualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    // Tratamento simples de erros
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> business(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }
}
