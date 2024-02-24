package com.mapwithplan.mapplan.config.ncloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * 버킷에 사용되는 설정 클래스 입니다.
 * String endPoint = "https://kr.object.ncloudstorage.com"; 파일을 저장하기 endpoint 필드입니다.
 * String regionName = "kr-standard"; 저장하기 위한 리전입니다.
 * S3Properties properties; 설정에 대한 주입 도메인 입니다.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class BucketConfig {

    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";

    private final S3Properties properties;

    public BucketConfig(S3Properties properties) {
        this.properties = properties;
    }

    /**
     * s3Client 를 생성하게 되었을 때 모든 설정을 주입후에 s3Client 를 리턴합니다.
     * 이때 필드에 있는 설정 값들을 주입합니다.
     * @return 모든 설정이 주입된 s3Client 를 리턴합니다.
     */
    @Bean
    public S3Client s3Client() {
        S3Client s3 = S3Client.builder()
                .endpointOverride(createURI(endPoint))
                .region(Region.of(regionName))
                .credentialsProvider(() -> AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey()))
                .build();

        // 버킷 생성
        createBucket(s3, properties.getBucketName());

        return s3;
    }

    /**
     * 버킷이 존재한지 확인합니다.
     * 없을시 버킷을 생성합니다. 만약에 이미 있다면, 존재하는 버킷을 return 합니다.
     * @param s3
     * @param bucketName 설정 파일에 있는 버킷 이름입니다.
     */
    private void createBucket(S3Client s3, String bucketName) {
        try {
            // 버킷이 존재하는지 확인
            HeadBucketResponse headBucketResponse = s3.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            if (headBucketResponse.sdkHttpResponse().isSuccessful()) {
                log.info("Bucket {} already exists.", bucketName);
            } else {
                // 버킷 생성
                s3.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket {} has been created.", bucketName);
            }
        } catch (S3Exception e) {
            // 서비스 요청이 실패한 경우
            e.printStackTrace();
        }
    }

    /**
     * endpoint 를 기반으로 URI 를 생성하는 메서드 입니다.
     * 잘못된 형식을 보낼시 에러를 catch 합니다.
     * @param endpoint
     * @return
     */
    private URI createURI(String endpoint) {
        try {
            return new URI(endpoint);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
