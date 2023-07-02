package br.com.aplicacao.metrics;

import br.com.aplicacao.config.CloudWatchBuilderStub;
import br.com.aplicacao.domain.CloudWatchClientWrapper;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorStatusMetric {

    private final CloudWatchClientWrapper cloudWatchClientWrapper;

    private static final String NAMESPACE = "ErrorMetrics";


    public void recordErrorMetric(int statusCode) {
        Dimension dimension = new Dimension()
                .withName("StatusCode")
                .withValue(String.valueOf(statusCode));


        MetricDatum datum = new MetricDatum()
                .withMetricName("Errors")
                .withUnit(StandardUnit.Milliseconds)
                .withValue(1.0)
                .withDimensions(dimension);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace(NAMESPACE)
                .withMetricData(datum);

        cloudWatchClientWrapper.putMetricData(CloudWatchBuilderStub.cloudWatchBuilder().putMetricData(request));
    }
}
