package app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.FuncionarioRequestDTO;
import app.dto.FuncionarioResponseDTO;
import app.entity.Departamento;
import app.entity.Funcionario;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.FuncionarioMapper;
import app.repository.DepartamentoRepository;
import app.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private FuncionarioMapper funcionarioMapper;

    @Autowired(required = false)
    private List<NotificacaoService> notificacoes;

    // ======================
    // CONSULTAS
    // ======================

    public List<FuncionarioResponseDTO> findAll() {
        return funcionarioRepository.findAllByOrderByNomeAsc()
                .stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }

    public List<FuncionarioResponseDTO> findAll(String cargo, Boolean ativo) {
        List<Funcionario> lista;

        if (cargo != null && !cargo.isBlank() && ativo != null) {
            lista = funcionarioRepository.findByCargoContainingIgnoreCaseAndAtivoOrderByNomeAsc(cargo.trim(), ativo);
        } else if (cargo != null && !cargo.isBlank()) {
            lista = funcionarioRepository.findByCargoContainingIgnoreCaseOrderByNomeAsc(cargo.trim());
        } else if (ativo != null) {
            lista = funcionarioRepository.findByAtivoOrderByNomeAsc(ativo);
        } else {
            lista = funcionarioRepository.findAllByOrderByNomeAsc();
        }

        return lista.stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }

    public FuncionarioResponseDTO findById(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        return funcionarioMapper.toResponseDTO(f);
    }
    
    public List<FuncionarioResponseDTO> findByDepartamento(Long departamentoId) {
        // Verifica se o departamento existe
        var departamento = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new NegocioException("Departamento não encontrado."));

        // Busca todos os funcionários vinculados a esse departamento
        List<Funcionario> funcionarios = funcionarioRepository.findByDepartamentoId(departamentoId);

        // Caso o departamento esteja inativo, apenas informa (não bloqueia a listagem)
        if (!departamento.getAtivo()) {
            System.out.println("Aviso: Departamento inativo — listando funcionários vinculados apenas para consulta.");
        }

        return funcionarios.stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }


    // ======================
    // CRIAR / ATUALIZAR / REMOVER
    // ======================

    @Transactional
    public FuncionarioResponseDTO save(FuncionarioRequestDTO dto) {
        validar(dto);

        // Busca e valida o departamento
        Departamento dep = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new NegocioException("Departamento não encontrado."));
        if (!dep.getAtivo()) {
            throw new NegocioException("Não é possível cadastrar funcionário em um departamento inativo.");
        }

        // Reativação de funcionário existente
        Optional<Funcionario> opt = funcionarioRepository.findByEmail(dto.email());
        if (opt.isPresent()) {
            Funcionario existente = opt.get();

            if (Boolean.TRUE.equals(existente.getAtivo())) {
                throw new NegocioException("E-mail já cadastrado.");
            }

            aplicarAtualizacao(dto, existente, true);
            existente.setDepartamento(dep);

            Funcionario reativado = funcionarioRepository.save(existente);
            notificar("Funcionário " + reativado.getNome() + " reativado.");
            return funcionarioMapper.toResponseDTO(reativado);
        }

        // Criação de novo funcionário
        Funcionario novo = funcionarioMapper.toEntity(dto);
        novo.setDepartamento(dep);
        if (dto.ativo() == null) novo.setAtivo(true);

        Funcionario salvo = funcionarioRepository.save(novo);
        notificar("Funcionário " + salvo.getNome() + " cadastrado.");
        return funcionarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public FuncionarioResponseDTO update(Long id, FuncionarioRequestDTO dto) {
        Funcionario atual = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        validar(dto);

        // Somente funcionários ativos podem ser editados
        if (!Boolean.TRUE.equals(atual.getAtivo())) {
            throw new NegocioException("Funcionário inativo não pode ser editado.");
        }

        // Valida departamento, se informado
        Departamento dep = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new NegocioException("Departamento não encontrado."));
        if (!dep.getAtivo()) {
            throw new NegocioException("Não é possível vincular funcionário a um departamento inativo.");
        }

        // E-mail duplicado
        if (dto.email() != null) {
            funcionarioRepository.findByEmail(dto.email()).ifPresent(outro -> {
                if (!outro.getId().equals(atual.getId()))
                    throw new NegocioException("E-mail já cadastrado.");
            });
        }

        // Salário não pode ser reduzido
        if (dto.salario() != null && atual.getSalario() != null && dto.salario() < atual.getSalario()) {
            throw new NegocioException("O salário não pode ser reduzido.");
        }

        aplicarAtualizacao(dto, atual, false);
        atual.setDepartamento(dep);

        Funcionario atualizado = funcionarioRepository.save(atual);
        notificar("Funcionário " + atualizado.getNome() + " atualizado.");
        return funcionarioMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public void delete(Long id) {
        Funcionario f = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionário não encontrado: " + id));

        funcionarioRepository.delete(f);
        notificar("Funcionário " + f.getNome() + " removido.");
    }

    @Transactional
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

    // ======================
    // MÉTODOS AUXILIARES
    // ======================

    private void aplicarAtualizacao(FuncionarioRequestDTO dto, Funcionario entidade, boolean reativacao) {
        funcionarioMapper.updateEntity(entidade, dto);
        if (reativacao) entidade.setAtivo(true);
    }

    private void validar(FuncionarioRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados do funcionário são obrigatórios.");
        }

        if (dto.nome() == null || dto.nome().isBlank() ||
            dto.email() == null || dto.email().isBlank() ||
            dto.cargo() == null || dto.cargo().isBlank()) {
            throw new NegocioException("Campos não podem estar vazios ou conter apenas espaços.");
        }

        if (dto.salario() == null || dto.salario() <= 0.0) {
            throw new NegocioException("Salário deve ser maior que zero.");
        }

        if (dto.dataAdmissao() == null || dto.dataAdmissao().isAfter(LocalDate.now())) {
            throw new NegocioException("Data de admissão não pode ser posterior à data atual.");
        }

        if (dto.departamentoId() == null) {
            throw new NegocioException("Departamento é obrigatório.");
        }
    }

    private void notificar(String msg) {
        if (notificacoes == null) return;
        for (NotificacaoService n : notificacoes) {
            try {
                n.mensagem(msg);
            } catch (Exception e) {
                System.out.println("Falha ao enviar mensagem: " + e.getMessage());
            }
        }
    }
}
