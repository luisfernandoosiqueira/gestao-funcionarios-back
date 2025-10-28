package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Departamento;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    // Lista departamentos por status (ativo/inativo)
    List<Departamento> findByAtivo(boolean ativo);

    // Verifica se já existe um departamento com o mesmo nome
    boolean existsByNome(String nome);
}
