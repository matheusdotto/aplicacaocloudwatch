package br.com.aplicacao.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@AllArgsConstructor
public class CloudWatchBuilderStub {

    @Value("${accessKey")
    private static String ACCESS_KEY;

    @Value("${secretKey")
    private static String SECRET_KEY;

    @Value("${endpoint}")
    private static String AWS_ENDPOINT;

    @Value("${region}")
    private static String AWS_REGION;

    private static final BasicAWSCredentials credentials = new BasicAWSCredentials("12345", "12345");


    public static AmazonCloudWatch cloudWatchBuilder () {


        return AmazonCloudWatchClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build();
    }
}
