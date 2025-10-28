package app.controller;

import app.dto.FuncionarioRequestDTO;
import app.dto.FuncionarioResponseDTO;
import app.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    // Lista todos (com filtros opcionais: cargo, ativo)
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarTodos(
            @RequestParam(required = false) String cargo,
            @RequestParam(required = false) Boolean ativo) {
        List<FuncionarioResponseDTO> lista = funcionarioService.findAll(cargo, ativo);
        return ResponseEntity.ok(lista);
    }

    // Lista por departamento (opcional)
    @GetMapping("/departamento/{id}")
    public ResponseEntity<List<FuncionarioResponseDTO>> listarPorDepartamento(@PathVariable Long id) {
        List<FuncionarioResponseDTO> lista = funcionarioService.findByDepartamento(id);
        return ResponseEntity.ok(lista);
    }

    // Busca por id
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable Long id) {
        FuncionarioResponseDTO dto = funcionarioService.findById(id);
        return ResponseEntity.ok(dto);
    }

    // Cria
    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> salvar(@RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO salvo = funcionarioService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // Atualiza
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid FuncionarioRequestDTO dto) {
        FuncionarioResponseDTO atualizado = funcionarioService.update(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // Inativa
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<FuncionarioResponseDTO> inativar(@PathVariable Long id) {
        FuncionarioResponseDTO dto = funcionarioService.inativar(id);
        return ResponseEntity.ok(dto);
    }

    // Remove
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        funcionarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
