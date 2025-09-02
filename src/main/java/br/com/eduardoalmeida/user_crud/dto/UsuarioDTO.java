package br.com.eduardoalmeida.user_crud.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Schema(description = "DTO para cadastro de Usuário")
public record UsuarioDTO(
        @Schema(description = "ID do usuário")
        Long id,

        @NotBlank
        @Schema(description = "Nome do usuário")
        String nome,

        @NotBlank
        @Email
        @Schema(description = "Email do usuário")
        String email,

        @Schema(description = "Data de criação do usuário")
        LocalDateTime dataCriacao
) {}