package com.gzxn.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ScaHistoryData {
    private Integer ID;
    private String INFRAS_CODE;
    private String METER_CODE;
    private String METER_PARAM_CODE;
    private String TYPE;
    private BigDecimal DATA_VALUE;
    private Date DATE_TIME;
}
