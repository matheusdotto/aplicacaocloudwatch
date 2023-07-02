package br.com.aplicacao.metrics;


import br.com.aplicacao.config.CloudWatchBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class LatencyInterceptor implements HandlerInterceptor {

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

        com.amazonaws.services.cloudwatch.model.MetricDatum datum = new com.amazonaws.services.cloudwatch.model.MetricDatum()
                .withMetricName("Latency")
                .withUnit(com.amazonaws.services.cloudwatch.model.StandardUnit.Microseconds)
                .withValue((double) latencyMillis);

        com.amazonaws.services.cloudwatch.model.PutMetricDataRequest metricData = new com.amazonaws.services.cloudwatch.model.PutMetricDataRequest()
                .withNamespace("LatencyRequest")
                .withMetricData(datum);

        CloudWatchBuilder.cloudWatchBuilder().putMetricData(metricData);
    }
}
