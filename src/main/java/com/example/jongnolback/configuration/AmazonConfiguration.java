package com.example.jongnolback.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
public class AmazonConfiguration {

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String regionName;

    @Value("${aws.endpoint}")
    private String endPoint;

    @Value("${aws.bucket-name}")
    private String bucketName;

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getBucketName() {
        return bucketName;
    }
}
