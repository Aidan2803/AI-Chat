package com.ether.ai_chat_server.controller;

import com.ether.ai_chat_server.dto.CurrentUserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/chat")
public class CurrentUserController {

    @GetMapping("/me")
    public CurrentUserDTO currentUser(Principal principal){
        return new CurrentUserDTO(principal.getName());
    }

}
