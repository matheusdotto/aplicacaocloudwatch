package br.com.aplicacao.controller;

import br.com.aplicacao.service.MetricsService;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/metricas")
@RestController
public class MetricsController {

    @Autowired
    private MetricsService metricsService;
    @GetMapping("/cpuUsage")
    public PutMetricDataResult cpuUsageMetric(){
        return metricsService.cpuUsage();
    }
}
