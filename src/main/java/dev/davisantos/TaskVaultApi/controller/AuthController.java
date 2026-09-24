package dev.davisantos.TaskVaultApi.controller;

import dev.davisantos.TaskVaultApi.dto.LoginRequestDTO;
import dev.davisantos.TaskVaultApi.dto.TokenResponseDTO;
import dev.davisantos.TaskVaultApi.dto.UserRequestDTO;
import dev.davisantos.TaskVaultApi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequestDTO dto){
        authService.register(dto);
        return ResponseEntity.ok().build(); //Return 200, to simplify
    }

}
