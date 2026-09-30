package dev.davisantos.TaskVaultApi.controller;

import dev.davisantos.TaskVaultApi.dto.LoginRequestDTO;
import dev.davisantos.TaskVaultApi.dto.TokenResponseDTO;
import dev.davisantos.TaskVaultApi.dto.UserRequestDTO;
import dev.davisantos.TaskVaultApi.dto.UserResponseDTO;
import dev.davisantos.TaskVaultApi.service.AuthService;
import dev.davisantos.TaskVaultApi.utils.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController implements GenericController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRequestDTO dto){
        UserResponseDTO response = authService.register(dto);
        URI uri = buildUri(response.id());
        return ResponseEntity.created(uri).build(); //Return 200, to simplify
    }
}
