package br.com.aplicacao.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudWatchConfiguration {

    private final String accessKey = "12345";

    private final String secretKey = "12345";


    public AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return credentials;
    }

    @Bean
    public AWSLogs awsLogs() {

        AWSLogs cloudWatchlog = AWSLogsClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://host.docker.internal:4566", "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .build();
        return cloudWatchlog;
    }
}