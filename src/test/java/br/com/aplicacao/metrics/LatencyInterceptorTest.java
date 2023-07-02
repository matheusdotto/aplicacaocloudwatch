package br.com.aplicacao.metrics;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import software.amazon.awssdk.services.cloudwatch.model.StandardUnit;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LatencyInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @Mock
    AmazonCloudWatch amazonCloudWatch;

    @Captor
    private ArgumentCaptor<PutMetricDataRequest> putMetricDataRequestCaptor;

    @InjectMocks
    private LatencyInterceptor latencyInterceptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        latencyInterceptor = new LatencyInterceptor();
    }

    @Test
    public void testPostHandle3() throws Exception {
        // Mock HttpServletRequest, HttpServletResponse e Object handler
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = new Object();

        // Criar uma instância de LatencyInterceptor
        LatencyInterceptor latencyInterceptor = new LatencyInterceptor();

        // Definir um Instant simulado para startTime
        Instant startTime = Instant.now();
        when(request.getAttribute("startTime")).thenReturn(startTime);

        // Chamar o método postHandle
        latencyInterceptor.postHandle(request, response, handler, null);

        // Verificar se putMetricData foi chamado com os parâmetros corretos
        ArgumentCaptor<PutMetricDataRequest> putMetricDataRequestCaptor = ArgumentCaptor.forClass(PutMetricDataRequest.class);
        verify(amazonCloudWatch, times(1)).putMetricData(putMetricDataRequestCaptor.capture());

        // Obter o argumento capturado
        PutMetricDataRequest capturedRequest = putMetricDataRequestCaptor.getValue();

        // Realizar asserções no request capturado
        assertEquals(1, capturedRequest.getMetricData().size());
        MetricDatum metricDatum = capturedRequest.getMetricData().get(0);
        assertEquals("Latency", metricDatum.getMetricName());
        assertEquals(StandardUnit.MILLISECONDS, metricDatum.getUnit());
        assertEquals(100.0, metricDatum.getValue());

        // Verificar se o atributo do request foi acessado
        verify(request, times(1)).getAttribute("startTime");
    }

}
