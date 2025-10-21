package app.dto;

import java.time.LocalDate;

public record FuncionarioResponseDTO(
    Long id,
    String nome,
    String email,
    String cargo,
    Double salario,
    LocalDate dataAdmissao,
    Boolean ativo
) {}
