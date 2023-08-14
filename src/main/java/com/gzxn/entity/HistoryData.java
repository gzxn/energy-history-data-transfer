package com.gzxn.entity;

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.Data;

import java.time.Instant;

@Data
@Measurement(name = "historydata")
public class HistoryData {
    @Column
    private String ID;
    @Column
    private String INFRAS_CODE;
    @Column(tag = true)
    private String METER_CODE;
    @Column(tag = true)
    private String METER_PARAM_CODE;
    @Column
    private String TYPE;
    @Column
    private String DATA_VALUE;
    @Column(timestamp = true)
    private Instant time;
}
