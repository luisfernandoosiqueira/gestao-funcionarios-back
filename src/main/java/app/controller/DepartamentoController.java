package app.controller;

import app.dto.DepartamentoRequestDTO;
import app.dto.DepartamentoResponseDTO;
import app.service.DepartamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    // Lista todos os departamentos
    @GetMapping
    public ResponseEntity<List<DepartamentoResponseDTO>> listarTodos() {
        List<DepartamentoResponseDTO> lista = departamentoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // Lista apenas os departamentos ativos
    @GetMapping("/ativos")
    public ResponseEntity<List<DepartamentoResponseDTO>> listarAtivos() {
        List<DepartamentoResponseDTO> lista = departamentoService.listarAtivos();
        return ResponseEntity.ok(lista);
    }

    // Busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        DepartamentoResponseDTO dto = departamentoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    // Cria novo departamento
    @PostMapping
    public ResponseEntity<DepartamentoResponseDTO> salvar(@RequestBody @Valid DepartamentoRequestDTO dto) {
        DepartamentoResponseDTO salvo = departamentoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // Atualiza um departamento existente
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DepartamentoRequestDTO dto) {
        DepartamentoResponseDTO atualizado = departamentoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // Inativa um departamento
    @PatchMapping("/{id}/inativar")
    public ResponseEntity<DepartamentoResponseDTO> inativar(@PathVariable Long id) {
        DepartamentoResponseDTO dto = departamentoService.inativar(id);
        return ResponseEntity.ok(dto);
    }
}
