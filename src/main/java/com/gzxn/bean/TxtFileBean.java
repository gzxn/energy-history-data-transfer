package com.gzxn.bean;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@Setter
public class TxtFileBean {
    private static final Logger log = LoggerFactory.getLogger(TxtFileBean.class);

    private String path;

    private File file;

    private TxtFileBean() {
    }

    public TxtFileBean(String path) {
        this.path = path;
        this.initFile();
    }

    /**
     * 初始化File
     */
    private void initFile() {
        this.file = new File(path);
        log.info("----------------配置文件: " + path + ", ===> " + file.exists());
    }

    /**
     * 读取配置文件内容
     *
     * @return content 内容
     */
    public String readConfigFile() {
        try {
            if (file.exists()) {
                byte[] fileBytes = Files.readAllBytes(Paths.get(path));
                return new String(fileBytes, StandardCharsets.UTF_8);
            }
            log.info("----------------配置文件: " + path + ", ===> " + file.exists());
            return null;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 写入配置文件内容
     *
     * @param content 内容
     */
    public void writeConfigFile(String content) {
        try {
            if (file.exists()) {
                Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));
            } else {
                log.info("----------------写入失败: " + path + ", ===> " + file.exists());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
