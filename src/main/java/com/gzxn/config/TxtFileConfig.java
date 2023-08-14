package com.gzxn.config;

import com.gzxn.bean.TxtFileBean;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class TxtFileConfig {
    private static final Logger log = LoggerFactory.getLogger(TxtFileConfig.class);
    /**
     * config.txt文件路径
     */
    @Value("${config.file.path}")
    private String path;

    @Bean
    public TxtFileBean pathConfig() {
        return new TxtFileBean(path);
    }

}
