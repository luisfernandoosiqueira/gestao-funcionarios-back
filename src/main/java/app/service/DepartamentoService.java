package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.DepartamentoRequestDTO;
import app.dto.DepartamentoResponseDTO;
import app.entity.Departamento;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.DepartamentoMapper;
import app.repository.DepartamentoRepository;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private DepartamentoMapper departamentoMapper;

    // ======================
    // CONSULTAS
    // ======================

    public List<DepartamentoResponseDTO> listarTodos() {
        return departamentoRepository.findAll()
                .stream()
                .map(departamentoMapper::toResponseDTO)
                .toList();
    }

    public List<DepartamentoResponseDTO> listarAtivos() {
        return departamentoRepository.findByAtivo(true)
                .stream()
                .map(departamentoMapper::toResponseDTO)
                .toList();
    }

    public DepartamentoResponseDTO buscarPorId(Long id) {
        Departamento d = departamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Departamento não encontrado: " + id));
        return departamentoMapper.toResponseDTO(d);
    }

    // ======================
    // CRIAR / ATUALIZAR / INATIVAR
    // ======================

    @Transactional
    public DepartamentoResponseDTO salvar(DepartamentoRequestDTO dto) {
        validar(dto);

        if (departamentoRepository.existsByNome(dto.nome())) {
            throw new NegocioException("Já existe um departamento com este nome.");
        }

        Departamento novo = departamentoMapper.toEntity(dto);
        if (dto.ativo() == null) novo.setAtivo(true);

        Departamento salvo = departamentoRepository.save(novo);
        return departamentoMapper.toResponseDTO(salvo);
    }

    @Transactional
    public DepartamentoResponseDTO atualizar(Long id, DepartamentoRequestDTO dto) {
        validar(dto);

        Departamento existente = departamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Departamento não encontrado: " + id));

        // Se tentar alterar o nome para outro já existente
        if (!existente.getNome().equalsIgnoreCase(dto.nome()) && 
            departamentoRepository.existsByNome(dto.nome())) {
            throw new NegocioException("Já existe um departamento com este nome.");
        }

        departamentoMapper.updateEntity(existente, dto);
        Departamento atualizado = departamentoRepository.save(existente);
        return departamentoMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public DepartamentoResponseDTO inativar(Long id) {
        Departamento d = departamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Departamento não encontrado: " + id));

        if (!d.getAtivo()) {
            throw new NegocioException("Este departamento já está inativo.");
        }

        d.setAtivo(false);
        Departamento salvo = departamentoRepository.save(d);
        return departamentoMapper.toResponseDTO(salvo);
    }

    // ======================
    // MÉTODOS AUXILIARES
    // ======================

    private void validar(DepartamentoRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados do departamento são obrigatórios.");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new NegocioException("Nome do departamento é obrigatório.");
        }

        if (dto.sigla() == null || dto.sigla().isBlank()) {
            throw new NegocioException("Sigla do departamento é obrigatória.");
        }
    }
}
