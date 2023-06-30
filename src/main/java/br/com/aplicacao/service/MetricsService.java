package br.com.aplicacao.service;

import br.com.aplicacao.metrics.CpuUsage;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MetricsService {

    BasicAWSCredentials credentials = new BasicAWSCredentials("12345", "12345");

    AmazonCloudWatch client = AmazonCloudWatchClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
            .build();

    public PutMetricDataResult cpuUsage(){

        double cpuUsage = CpuUsage.getCPULoad();

        MetricDatum cpuUse = new MetricDatum()
                .withMetricName("CPUUsage")
                .withUnit(StandardUnit.Percent)
                .withValue(cpuUsage);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("CpuMetric")
                .withMetricData(cpuUse);

        return client.putMetricData(request);
    }
}
