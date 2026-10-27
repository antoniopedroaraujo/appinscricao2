package com.aula.pos.appinscricao.dto;

public record LoginRequest(
        String email,
        String password
) {
}
