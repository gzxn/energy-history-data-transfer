package com.gzxn.config;

import com.gzxn.bean.InfluxDbBean;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix = "influx2")
@PropertySource(value = "classpath:influx2.properties")
public class InfluxDbConfig {
    static final Logger log = LoggerFactory.getLogger(InfluxDbConfig.class);

    /**
     * 数据库url地址
     */
    private String url;
    /**
     * 桶(表)
     */
    private String bucket;
    /**
     * 组织
     */
    private String org;
    /**
     * token
     */
    private String token;

    @Bean
    public InfluxDbBean influxDBClient() {
        return new InfluxDbBean(url, token, org, bucket);
    }

}
