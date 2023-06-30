package br.com.aplicacao.controller;

import br.com.aplicacao.domain.Aluno;
import br.com.aplicacao.repository.AlunoRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final Counter totalAlunosMetric;

    @Autowired
    public AlunoController(MeterRegistry meterRegistry) {
        this.totalAlunosMetric = Counter.builder("total_alunos")
                .description("Total de alunos registrados")
                .register(meterRegistry);
    }

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping
    public String getAlunos() {
        // Lógica para obter todos os alunos do banco de dados
        return "Lista de alunos";
    }

    @GetMapping("/{id}")
    public Optional<Aluno> getAluno(@PathVariable Long id) {
        return Optional.ofNullable(alunoRepository.findById(id).orElseThrow(() -> new RuntimeException("Aluno não encontrado")));

    }

    @PostMapping
    public Aluno createAluno(@RequestBody Aluno aluno) {
        alunoRepository.save(aluno);
        totalAlunosMetric.increment();
        return aluno;
    }

    @PutMapping("/{id}")
    public String updateAluno(@PathVariable String id, @RequestBody Aluno aluno) {
        // Lógica para atualizar um aluno existente no banco de dados
        return "Aluno atualizado";
    }

    @PatchMapping("/{id}")
    public String partialUpdateAluno(@PathVariable String id, @RequestBody Aluno aluno) {
        // Lógica para atualizar parcialmente um aluno existente no banco de dados
        return "Aluno parcialmente atualizado";
    }

    @DeleteMapping("/{id}")
    public String deleteAluno(@PathVariable String id) {
        // Lógica para excluir um aluno do banco de dados
        return "Aluno excluído";
    }
}
