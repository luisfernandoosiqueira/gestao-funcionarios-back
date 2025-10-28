package app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartamentoRequestDTO(

    @NotBlank
    @Size(min = 3, max = 120)
    String nome,

    @NotBlank
    @Size(min = 2, max = 10)
    String sigla,

    // opcional
    Boolean ativo
) {}
