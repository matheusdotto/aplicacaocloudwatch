package br.com.aplicacao.metrics;

import br.com.aplicacao.domain.CloudWatchClientWrapper;
import br.com.aplicacao.stub.CloudWatchBuilderStubTest;
import com.amazonaws.services.cloudwatch.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ErrorStatusMetricsTest {

    private CloudWatchClientWrapper cloudWatchClientWrapper;
    private ErrorStatusMetric errorStatusMetric;

    @BeforeEach
    public void setup() {
        cloudWatchClientWrapper = mock(CloudWatchClientWrapper.class);
        errorStatusMetric = new ErrorStatusMetric(cloudWatchClientWrapper);
    }

    @Test
    public void testRecordErrorMetric() {
        int statusCode = 500;

        PutMetricDataRequest request = new PutMetricDataRequest();
        PutMetricDataResult response = new PutMetricDataResult();

    }
}
