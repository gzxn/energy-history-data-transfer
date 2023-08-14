package com.gzxn.dao;

import com.gzxn.entity.ScaHistoryData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ScaHistoryDataDao {

    @Select("SELECT * FROM t_sca_history_data WHERE DATE_TIME = '${datetime}' ORDER BY ID ASC")
    List<ScaHistoryData> queryScaHistoryDataByDateTime(String datetime);

    @Select("SELECT * FROM t_sca_history_data WHERE ID = ${id}")
    ScaHistoryData queryScaHistoryDataById(Integer id);

    @Select("SELECT * FROM t_sca_history_data WHERE ID >= ${startId} AND ID < ${endId}")
    List<ScaHistoryData> queryScaHistoryListById(Integer startId, Integer endId);

}
