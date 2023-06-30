package br.com.aplicacao.controller;

import br.com.aplicacao.domain.Aluno;
import br.com.aplicacao.repository.AlunoRepository;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.cloudwatch.model.Dimension;

import java.util.Optional;

import static br.com.aplicacao.metrics.CpuUsage.getCPULoad;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final Counter totalAlunosMetric;

    BasicAWSCredentials credentials = new BasicAWSCredentials("12345", "12345");

    AmazonCloudWatch client = AmazonCloudWatchClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
            .build();


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
        MetricDatum datum = new MetricDatum()
                .withMetricName("Alunos")
                .withUnit("Count")
                .withValue(totalAlunosMetric.count());

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("TesteMetrica")
                .withMetricData(datum);

        client.putMetricData(request);
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
