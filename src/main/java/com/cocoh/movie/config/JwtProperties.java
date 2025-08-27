package com.cocoh.movie.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component // 클래스를 Spring 관리 빈으로 표시하기 위한 범용 어노테이션
@ConfigurationProperties("jwt") // properties에 있는 값을 읽어오는 어노테이션
public class JwtProperties {
    private String issuer;
    private String secretKey;
    private int duration;
    private int refreshDuration;
}
