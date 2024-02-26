package com.mapwithplan.mapplan.config.ncloud;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.stereotype.Component;


/**
 * yml 에 있는 속성을 가져오는 객체입니다.
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties("spring.s3")
public class S3Properties {


    private String accessKey;

    private String secretKey;

    private String bucketName;
    public S3Properties(String accessKey, String secretKey, String bucketName) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
    }
}
