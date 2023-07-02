package br.com.aplicacao.domain;

import com.amazonaws.services.cloudwatch.model.PutMetricDataResult;

public interface CloudWatchClientWrapper {
    PutMetricDataResult putMetricData(PutMetricDataResult request);

}
