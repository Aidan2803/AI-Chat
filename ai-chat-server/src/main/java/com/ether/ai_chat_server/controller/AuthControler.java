package com.ether.ai_chat_server.controller;

import com.ether.ai_chat_server.dto.RegisterRequest;
import com.ether.ai_chat_server.model.ApplicationUser;
import com.ether.ai_chat_server.repository.ApplicationUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthControler {
    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthControler(ApplicationUserRepository applicationUserRepository,
                         PasswordEncoder passwordEncoder){
        this.userRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        String username = registerRequest.username() == null ? "" : registerRequest.username().trim();
        if (username.length() < 3 || registerRequest.password().isEmpty() || registerRequest.password().length() < 6){
            return ResponseEntity.badRequest().body("Username minimum 3 characters, password minimum 6");
        }

        if(userRepository.existsByUsername(username)){
            return ResponseEntity.badRequest().body("Username already taken");
        }

        ApplicationUser user = new ApplicationUser(username, passwordEncoder.encode(registerRequest.password()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
