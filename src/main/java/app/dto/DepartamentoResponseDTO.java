package app.dto;

public record DepartamentoResponseDTO(
    Long id,
    String nome,
    String sigla,
    Boolean ativo
) {}
