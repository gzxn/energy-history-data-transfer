package com.gzxn.scheduled;

import com.gzxn.mode.HistoryToInfluxDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScaHistoryDataScheduled {
    private static final Logger log = LoggerFactory.getLogger(ScaHistoryDataScheduled.class);

    /**
     * 每分钟运行一次，同步Mysql数据到InfluxDb中
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void builder() {
        HistoryToInfluxDb historyToInfluxDb = HistoryToInfluxDb.getInstance();
        historyToInfluxDb.sync();
    }

}
