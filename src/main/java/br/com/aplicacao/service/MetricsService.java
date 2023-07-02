package br.com.aplicacao.service;

import br.com.aplicacao.config.CloudWatchBuilderStub;
import br.com.aplicacao.metrics.CpuUsage;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetricsService {

    public PutMetricDataResult cpuUsage(){

        double cpuUsage = CpuUsage.getCPULoad();

        MetricDatum cpuUse = new MetricDatum()
                .withMetricName("CPUUsage")
                .withUnit(StandardUnit.Percent)
                .withValue(cpuUsage);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("CpuMetric")
                .withMetricData(cpuUse);

        return CloudWatchBuilderStub.cloudWatchBuilder().putMetricData(request);
    }
}
