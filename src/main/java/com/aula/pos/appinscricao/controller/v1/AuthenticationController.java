package com.aula.pos.appinscricao.controller.v1;

import com.aula.pos.appinscricao.dto.LoginRequest;
import com.aula.pos.appinscricao.model.Usuario;
import com.aula.pos.appinscricao.service.impl.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        var credenciais = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        System.out.println(credenciais);
        var auth = authenticationManager.authenticate(credenciais);

        var usuario = (Usuario) auth.getPrincipal();
        return new TokenResponse(tokenService.generateToken(usuario));
    }
}
