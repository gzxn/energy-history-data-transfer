package com.gzxn.utils;

import java.sql.Timestamp;

/**
 * 时间工具类
 */
public class DateUtil {

    public Timestamp convertTimestamp(String timestampStr) {
        if ("0000-00-00 00:00:00".equals(timestampStr)) {
            return null;
        } else {
            return Timestamp.valueOf(timestampStr);
        }
    }

}
