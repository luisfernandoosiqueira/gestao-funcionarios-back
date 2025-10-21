package app.repository;

import app.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Funcionario> findByAtivo(boolean ativo);

    List<Funcionario> findByCargoContainingIgnoreCase(String cargo);

    List<Funcionario> findByCargoContainingIgnoreCaseAndAtivo(String cargo, boolean ativo);
}
