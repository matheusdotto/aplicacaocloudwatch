package br.com.aplicacao.metrics;


import br.com.aplicacao.config.CloudWatchBuilderStub;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
public class LatencyInterceptor implements HandlerInterceptor {

    private static final int TOTAL_METRICS = 51; // Total de estágios de latência (0ms até 100ms com intervalo de 5ms)
    private static final int INTERVAL = 100; // Intervalo entre os estágios de latência em ms

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Antes de processar a requisição

        // Registrar o tempo de início da requisição
        request.setAttribute("startTime", System.nanoTime());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Após processar a requisição

        // Calcular a latência da requisição em milissegundos

        long startTime = (long) request.getAttribute("startTime");
        long endTime = System.nanoTime();
        long latencyMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);


        // Enviar a métrica de latência para o CloudWatch

        int latencyStage = (int) (latencyMillis / INTERVAL); // Calcular o estágio de latência com base no intervalo

        if (latencyStage >= TOTAL_METRICS) {
            latencyStage = TOTAL_METRICS - 1; // Garantir que estágios maiores que o total sejam ajustados para o último estágio
        }

        MetricDatum datum = new MetricDatum()
                .withMetricName("Latency")
                .withUnit(StandardUnit.Milliseconds)
                .withValue((double) latencyMillis);

        // Criar dimensão para representar o estágio de latência
        Dimension stageDimension = new Dimension()
                .withName("LatencyStage")
                .withValue(String.valueOf(latencyStage));

        datum.setDimensions(Collections.singletonList(stageDimension));

        PutMetricDataRequest metricData = new PutMetricDataRequest()
                .withNamespace("LatencyRequest")
                .withMetricData(datum);

        CloudWatchBuilderStub.cloudWatchBuilder().putMetricData(metricData);
    }
}
