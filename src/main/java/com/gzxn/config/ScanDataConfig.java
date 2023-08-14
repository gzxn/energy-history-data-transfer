package com.gzxn.config;

import com.gzxn.bean.InfluxDbBean;
import com.gzxn.bean.TxtFileBean;
import com.gzxn.dao.ScaHistoryDataDao;
import com.gzxn.mode.HistoryToInfluxDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 单例类Bean初始化
 */
@Configuration
public class ScanDataConfig {

    @Autowired
    private InfluxDbBean influxDbBean;

    @Autowired
    private TxtFileBean txtFileBean;

    @Autowired
    private ScaHistoryDataDao historyDataDao;

    @Bean
    public HistoryToInfluxDb historyToInfluxDb() {
        HistoryToInfluxDb historyToInfluxDb = HistoryToInfluxDb.getInstance();
        historyToInfluxDb.setInfluxDbBean(influxDbBean);
        historyToInfluxDb.setTxtFileBean(txtFileBean);
        historyToInfluxDb.setHistoryDataDao(historyDataDao);
        return historyToInfluxDb;
    }

}
