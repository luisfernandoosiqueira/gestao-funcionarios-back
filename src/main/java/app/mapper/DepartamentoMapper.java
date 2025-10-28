package app.mapper;

import org.springframework.stereotype.Component;

import app.dto.DepartamentoRequestDTO;
import app.dto.DepartamentoResponseDTO;
import app.entity.Departamento;

@Component
public class DepartamentoMapper {

    public Departamento toEntity(DepartamentoRequestDTO dto) {
        if (dto == null) return null;

        Departamento d = new Departamento();
        d.setNome(dto.nome());
        d.setSigla(dto.sigla());
        if (dto.ativo() != null) {
            d.setAtivo(dto.ativo());
        }
        return d;
    }

    public DepartamentoResponseDTO toResponseDTO(Departamento d) {
        if (d == null) return null;

        return new DepartamentoResponseDTO(
                d.getId(),
                d.getNome(),
                d.getSigla(),
                d.getAtivo()
        );
    }

    public void updateEntity(Departamento destino, DepartamentoRequestDTO dto) {
        if (destino == null || dto == null) return;

        if (dto.nome() != null)  destino.setNome(dto.nome());
        if (dto.sigla() != null) destino.setSigla(dto.sigla());
        if (dto.ativo() != null) destino.setAtivo(dto.ativo());
    }
}
