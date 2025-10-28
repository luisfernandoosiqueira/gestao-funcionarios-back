package app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record FuncionarioRequestDTO(

    @NotBlank
    @Size(min = 3, max = 120)
    String nome,

    @NotBlank
    @Email
    @Size(max = 120)
    String email,

    @NotBlank
    @Size(min = 2, max = 60)
    String cargo,

    @NotNull
    @Positive
    Double salario,

    @NotNull
    @PastOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dataAdmissao,

    // opcional
    Boolean ativo,

    @NotNull(message = "O departamento é obrigatório")
    Long departamentoId
) {}
