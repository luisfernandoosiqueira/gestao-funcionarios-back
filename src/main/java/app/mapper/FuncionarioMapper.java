package app.mapper;

import org.springframework.stereotype.Component;

import app.dto.FuncionarioRequestDTO;
import app.dto.FuncionarioResponseDTO;
import app.entity.Funcionario;

@Component
public class FuncionarioMapper {

    public Funcionario toEntity(FuncionarioRequestDTO dto) {
        if (dto == null) return null;

        Funcionario f = new Funcionario();
        f.setNome(dto.nome());
        f.setEmail(dto.email());
        f.setCargo(dto.cargo());
        f.setSalario(dto.salario());
        f.setDataAdmissao(dto.dataAdmissao());
        if (dto.ativo() != null) {
            f.setAtivo(dto.ativo());
        }
        return f;
    }

    public FuncionarioResponseDTO toResponseDTO(Funcionario f) {
        if (f == null) return null;

        return new FuncionarioResponseDTO(
                f.getId(),
                f.getNome(),
                f.getEmail(),
                f.getCargo(),
                f.getSalario(),
                f.getDataAdmissao(),
                f.getAtivo()
        );
    }

 
    public void updateEntity(Funcionario destino, FuncionarioRequestDTO dto) {
        if (destino == null || dto == null) return;

        if (dto.nome() != null)         destino.setNome(dto.nome());
        if (dto.email() != null)        destino.setEmail(dto.email());
        if (dto.cargo() != null)        destino.setCargo(dto.cargo());
        if (dto.salario() != null)      destino.setSalario(dto.salario());
        if (dto.dataAdmissao() != null) destino.setDataAdmissao(dto.dataAdmissao());
        if (dto.ativo() != null)        destino.setAtivo(dto.ativo());
    }
}
