package app.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dto.FuncionarioRequestDTO;
import app.dto.FuncionarioResponseDTO;
import app.entity.Funcionario;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.FuncionarioMapper;
import app.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired private FuncionarioRepository funcionarioRepository;
    @Autowired private FuncionarioMapper funcionarioMapper;
    @Autowired(required = false) private List<NotificacaoService> notificacoes;

    // consultas
    public List<FuncionarioResponseDTO> findAll() {
        return funcionarioRepository.findAll().stream()
                .sorted(Comparator.comparing(
                        f -> f.getNome() == null ? "" : f.getNome(),
                        (a, b) -> a.compareToIgnoreCase(b)))
                .map(f -> funcionarioMapper.toResponseDTO(f))
                .toList();
    }

    // consultas com filtros (?cargo= & ?ativo=)
    public List<FuncionarioResponseDTO> findAll(String cargo, Boolean ativo) {
        List<Funcionario> base;
        if (cargo != null && !cargo.isBlank() && ativo != null) {
            base = funcionarioRepository.findByCargoContainingIgnoreCaseAndAtivo(cargo, ativo);
        } else if (cargo != null && !cargo.isBlank()) {
            base = funcionarioRepository.findByCargoContainingIgnoreCase(cargo);
        } else if (ativo != null) {
            base = funcionarioRepository.findByAtivo(ativo);
        } else {
            base = funcionarioRepository.findAll();
        }

        return base.stream()
                .sorted(Comparator.comparing(
                        f -> f.getNome() == null ? "" : f.getNome(),
                        (a, b) -> a.compareToIgnoreCase(b)))
                .map(f -> funcionarioMapper.toResponseDTO(f))
                .toList();
    }

    public FuncionarioResponseDTO findById(Long id) {
        if (id == null) throw new NegocioException("Id do funcionário é obrigatório.");
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));
        return funcionarioMapper.toResponseDTO(f);
    }

    // criar / atualizar / remover
    public FuncionarioResponseDTO save(FuncionarioRequestDTO dto) {
        validar(dto, false);

        // reativação por e-mail
        Optional<Funcionario> opt = funcionarioRepository.findByEmail(dto.email());
        if (opt.isPresent()) {
            Funcionario existente = opt.get();

            if (Boolean.TRUE.equals(existente.getAtivo())) {
                throw new NegocioException("E-mail já cadastrado.");
            }

            funcionarioMapper.updateEntity(existente, dto); // patch campos do DTO
            existente.setAtivo(true);

            validar(existente, true);
            Funcionario reativado = funcionarioRepository.save(existente);
            notificar("Funcionário " + reativado.getNome() + " reativado.");
            return funcionarioMapper.toResponseDTO(reativado);
        }

        // criação
        Funcionario novo = funcionarioMapper.toEntity(dto);
        if (dto.ativo() == null) novo.setAtivo(true);

        validar(novo, true);
        Funcionario salvo = funcionarioRepository.save(novo);
        notificar("Funcionário " + salvo.getNome() + " cadastrado.");
        return funcionarioMapper.toResponseDTO(salvo);
    }

    public FuncionarioResponseDTO update(Long id, FuncionarioRequestDTO dto) {
        Funcionario atual = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        if (dto == null) throw new NegocioException("Dados do funcionário são obrigatórios.");
        validar(dto, true); // valida apenas campos presentes

        // e-mail único no update
        if (dto.email() != null) {
            funcionarioRepository.findByEmail(dto.email()).ifPresent(outro -> {
                if (!outro.getId().equals(atual.getId()))
                    throw new NegocioException("E-mail já cadastrado.");
            });
        }

        // não reduzir salário
        if (dto.salario() != null) {
            if (atual.getSalario() != null && dto.salario() < atual.getSalario())
                throw new NegocioException("O salário não pode ser reduzido.");
        }

        // patch via mapper
        funcionarioMapper.updateEntity(atual, dto);

        validar(atual, true);
        Funcionario atualizado = funcionarioRepository.save(atual);
        notificar("Funcionário " + atualizado.getNome() + " atualizado.");
        return funcionarioMapper.toResponseDTO(atualizado);
    }

    public void delete(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));
        funcionarioRepository.delete(f);
        notificar("Funcionário " + f.getNome() + " removido.");
    }

    // inativação (patch)
    public FuncionarioResponseDTO inativar(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        if (Boolean.FALSE.equals(f.getAtivo())) {
            return funcionarioMapper.toResponseDTO(f); // idempotente
        }

        f.setAtivo(false);
        Funcionario salvo = funcionarioRepository.save(f);
        notificar("Funcionário " + salvo.getNome() + " inativado.");
        return funcionarioMapper.toResponseDTO(salvo);
    }

    // valida payload (DTO)
    private void validar(FuncionarioRequestDTO dto, boolean update) {
        if (dto == null) throw new NegocioException("Dados do funcionário são obrigatórios.");

        if (!update) {
            if (dto.nome() == null || dto.nome().isBlank())
                throw new NegocioException("Nome é obrigatório.");
            if (dto.email() == null || dto.email().isBlank())
                throw new NegocioException("E-mail é obrigatório.");
            if (dto.cargo() == null || dto.cargo().isBlank())
                throw new NegocioException("Cargo é obrigatório.");
            if (dto.salario() == null || dto.salario() <= 0.0)
                throw new NegocioException("Salário deve ser maior que zero.");
            if (dto.dataAdmissao() == null || dto.dataAdmissao().isAfter(LocalDate.now()))
                throw new NegocioException("Data de admissão inválida.");
        } else {
            if (dto.nome() != null && dto.nome().isBlank())
                throw new NegocioException("Nome inválido.");
            if (dto.email() != null && dto.email().isBlank())
                throw new NegocioException("E-mail inválido.");
            if (dto.cargo() != null && dto.cargo().isBlank())
                throw new NegocioException("Cargo inválido.");
            if (dto.salario() != null && dto.salario() <= 0.0)
                throw new NegocioException("Salário deve ser maior que zero.");
            if (dto.dataAdmissao() != null && dto.dataAdmissao().isAfter(LocalDate.now()))
                throw new NegocioException("Data de admissão inválida.");
        }
    }

    // valida entidade final (após montar/patch)
    private void validar(Funcionario f, boolean update) {
        if (f == null) throw new NegocioException("Funcionário é obrigatório.");
        if (f.getNome() == null || f.getNome().isBlank())
            throw new NegocioException("Nome é obrigatório.");
        if (f.getEmail() == null || f.getEmail().isBlank())
            throw new NegocioException("E-mail é obrigatório.");
        if (f.getCargo() == null || f.getCargo().isBlank())
            throw new NegocioException("Cargo é obrigatório.");
        if (f.getSalario() == null || f.getSalario() <= 0.0)
            throw new NegocioException("Salário deve ser maior que zero.");
        if (f.getDataAdmissao() == null || f.getDataAdmissao().isAfter(LocalDate.now()))
            throw new NegocioException("Data de admissão inválida.");
    }

    // notificação (opcional)
    private void notificar(String msg) {
        if (notificacoes == null) return;
        for (NotificacaoService n : notificacoes) {
            try { n.mensagem(msg); } catch (Exception e) { System.out.println("Falha ao enviar mensagem: " + e); }
        }
    }
}
