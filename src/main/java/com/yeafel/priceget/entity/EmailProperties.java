package com.yeafel.priceget.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "email")
public class EmailProperties {
    private String host;
    private String userName;
    private String passWord;
    private String toEmail;
    private String toName;


}