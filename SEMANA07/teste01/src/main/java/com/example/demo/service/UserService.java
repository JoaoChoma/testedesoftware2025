// src/main/java/com/example/demo/service/UserService.java
package com.example.demo.service;


import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    // Injeção de dependências via construtor (boa prática)
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerNewUser(UserDto registrationDto) {

        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new IllegalStateException("Erro: Nome de usuário '" + registrationDto.getUsername() + "' já está em uso.");
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        return userRepository.save(newUser);
    }
}