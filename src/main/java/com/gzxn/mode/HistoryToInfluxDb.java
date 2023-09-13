package com.gzxn.mode;

import com.gzxn.bean.InfluxDbBean;
import com.gzxn.bean.TxtFileBean;
import com.gzxn.dao.ScaHistoryDataDao;
import com.gzxn.entity.HistoryData;
import com.gzxn.entity.ScaHistoryData;
import com.gzxn.utils.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HistoryToInfluxDb extends SyncData implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(HistoryToInfluxDb.class);

    private static HistoryToInfluxDb historyToInfluxDb = null;

    private static Thread thread = null;

    private InfluxDbBean influxDbBean;

    private TxtFileBean txtFileBean;

    private ScaHistoryDataDao historyDataDao;
    // ID
    private Integer ID;
    // 检测通过标志
    private boolean checkOkFlag = false;
    // 同步完成标志
    private boolean completeFlag = true;

    private HistoryToInfluxDb() {
    }

    public static HistoryToInfluxDb getInstance() {
        if (historyToInfluxDb == null) {
            historyToInfluxDb = new HistoryToInfluxDb();
            thread = new Thread(historyToInfluxDb);
        }
        return historyToInfluxDb;
    }

    @Override
    protected void check() {
        if (influxDbBean == null || txtFileBean == null || historyDataDao == null) {
            log.warn("----------------Bean对象初始化出错，请重启项目并检查InfluxDb连接状况");
            return;
        }
        if (influxDbBean.getClient() == null) {
            log.warn("----------------无法连接InfluxDB服务器，请检查");
            return;
        }
        if (txtFileBean.getFile() == null) {
            log.warn("----------------获取配置文件出错，请检查");
            return;
        }
        if (!txtFileBean.getFile().exists()) {
            log.warn("----------------配置文件不存在，请检查");
            return;
        }
        // 检测通过
        this.setCheckOkFlag(true);
    }

    @Override
    protected void init() {
        this.checkOkFlag = false;
        this.completeFlag = false;
        // 获取ID
        ID = Integer.parseInt(txtFileBean.readConfigFile());
    }

    @Override
    protected void startSync() {
        Thread.State state = thread.getState();
        if (state == Thread.State.NEW) {
            thread.start();
        }
        if (!completeFlag && state == Thread.State.TERMINATED) {
            thread = new Thread(historyToInfluxDb);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            log.info("----------------开始同步新数据");
            while (!completeFlag) {
                List<ScaHistoryData> scaHistoryDataList = historyDataDao.queryScaHistoryListById(ID, ID + 150000);
                log.info("startID: " + ID + ", endID: " + (ID + 150000));
                if (scaHistoryDataList.size() > 0) {
                    List<HistoryData> historyDataList = new ArrayList<>(8);
                    for (ScaHistoryData scaHistoryData : scaHistoryDataList) {
                        HistoryData historyData = new HistoryData();
                        historyData.setID(scaHistoryData.getID().toString());
                        historyData.setINFRAS_CODE(scaHistoryData.getINFRAS_CODE());
                        historyData.setMETER_CODE(scaHistoryData.getMETER_CODE());
                        historyData.setMETER_PARAM_CODE(scaHistoryData.getMETER_PARAM_CODE());
                        historyData.setTYPE(scaHistoryData.getTYPE());
                        historyData.setDATA_VALUE(scaHistoryData.getDATA_VALUE().toString());
                        historyData.setTime(scaHistoryData.getDATE_TIME().toInstant());
                        historyDataList.add(historyData);
                    }
                    influxDbBean.writeSynchronousBatch(historyDataList);
                    ID = scaHistoryDataList.get(scaHistoryDataList.size() - 1).getID() + 1;
                    txtFileBean.writeConfigFile(ID.toString());
                } else {
                    this.completeFlag = true;
                    log.info("----------------新同步数据完成");
                }
            }
        } catch (Exception e) {
            this.completeFlag = true;
            log.error(ExceptionUtil.getExceptionInfo(e));
        }
    }

    @Override
    protected boolean isCheckOk() {
        return checkOkFlag;
    }

    @Override
    protected boolean isComplete() {
        return completeFlag;
    }

    public void setCheckOkFlag(boolean checkOkFlag) {
        this.checkOkFlag = checkOkFlag;
    }

    public void setCompleteFlag(boolean completeFlag) {
        this.completeFlag = completeFlag;
    }

    public void setInfluxDbBean(InfluxDbBean influxDbBean) {
        this.influxDbBean = influxDbBean;
    }

    public void setTxtFileBean(TxtFileBean txtFileBean) {
        this.txtFileBean = txtFileBean;
    }

    public void setHistoryDataDao(ScaHistoryDataDao historyDataDao) {
        this.historyDataDao = historyDataDao;
    }

}
