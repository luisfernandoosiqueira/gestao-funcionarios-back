package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Funcionario> findByAtivo(boolean ativo);

    List<Funcionario> findByCargoContainingIgnoreCase(String cargo);

    List<Funcionario> findByCargoContainingIgnoreCaseAndAtivo(String cargo, boolean ativo);

    List<Funcionario> findByDepartamentoId(Long departamentoId);

    List<Funcionario> findByDepartamentoNome(String nome);

    List<Funcionario> findByDepartamentoSigla(String sigla);

    List<Funcionario> findByDepartamentoAtivo(boolean ativo);

    // ========= MÉTODOS COM ORDENAÇÃO =========
    List<Funcionario> findAllByOrderByNomeAsc();

    List<Funcionario> findByAtivoOrderByNomeAsc(boolean ativo);

    List<Funcionario> findByCargoContainingIgnoreCaseOrderByNomeAsc(String cargo);

    List<Funcionario> findByCargoContainingIgnoreCaseAndAtivoOrderByNomeAsc(String cargo, boolean ativo);
}
