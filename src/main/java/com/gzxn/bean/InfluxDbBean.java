package com.gzxn.bean;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.Bucket;
import com.influxdb.client.domain.Organization;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.influxdb.query.dsl.Flux;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
public class InfluxDbBean {
    private static final Logger log = LoggerFactory.getLogger(InfluxDbBean.class);

    private String url;
    private String bucket;
    private String org;
    private String token;
    private InfluxDBClient client;  // InfluxDB数据库连接

    private InfluxDbBean() {
    }

    public InfluxDbBean(String url, String token, String org, String bucket) {
        this.url = url;
        this.token = token;
        this.org = org;
        this.bucket = bucket;
        this.client = createClient();
    }

    /**
     * 创建连接
     */
    private InfluxDBClient createClient() {
        try {
            if (client == null) {
                log.info("----------------开始创建InfluxDbBean");
                client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
                if (!client.ping()) {
                    log.error("----------------无法连接InfluxDb");
                    return null;
                }
                String orgId = getOrgId();
                if (orgId == null) {
                    log.error("----------------组织创建失败: " + org);
                    return null;
                }
                log.info("----------------组织已创建：" + org);
                boolean hasBucket = isExistBucketByBucketApi();
                log.info("----------------检查是否存在bucket: " + bucket + ", ===> " + hasBucket);
                if (!hasBucket) {
                    log.info("----------------开始创建bucket: " + bucket);
                    Bucket createBucket = new Bucket();
                    createBucket.setName(bucket);
                    createBucket.setOrgID(orgId);
                    Bucket bk = client.getBucketsApi().createBucket(createBucket);
                    log.info("----------------创建bucket完成: " + bk.getName());
                }
                log.info("----------------成功创建InfluxDb连接");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            client = null;
        }
        return client;
    }

    /**
     * 同步Mysql数据到InfluxDb
     */
    private void TransferHistoryData() {
        Flux flux = Flux.from(bucket).expression("|> range(start: 0)");
        log.info("----------------查询InfluxDb是否有数据: ");

    }

    /**
     * 获取OrgId
     *
     * @return
     */
    public String getOrgId() {
        String orgId = null;
        try {
            List<Organization> organizationList = client.getOrganizationsApi().findOrganizations();
            if (organizationList.size() > 0) {
                for (Organization organization : organizationList) {
                    String orgName = organization.getName();
                    if (org.equals(orgName)) {
                        orgId = organization.getId();
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return orgId;
    }

    /**
     * 通过BucketApi查找bucket是否存在
     *
     * @return
     */
    public boolean isExistBucketByBucketApi() {
        Bucket bk = client.getBucketsApi().findBucketByName(bucket);
        return bk != null;
    }

    /**
     * 通过QueryApi查找bucket是否存在
     *
     * @return
     */
    public boolean isExistBucketByQueryApi() {
        boolean flag = false;
        String fluxQuery = "buckets()";
        List<FluxTable> fluxTableList = queryFluxTable(fluxQuery);
        for (FluxTable table : fluxTableList) {
            for (FluxRecord record : table.getRecords()) {
                String name = record.getValues().get("name").toString();
                if (bucket.equals(name)) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 写入数据-同步
     * <p>
     * 以秒为单位：WritePrecision.S
     * </p>
     *
     * @param object
     */
    public void writeSynchronous(Object object) {
        try {
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writeMeasurement(bucket, org, WritePrecision.S, object);
            log.info("---------------同步写入InfluxDB成功");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 批量写入数据-同步
     * <p>
     * 以秒为单位：WritePrecision.S
     * </p>
     *
     * @param objects
     */
    public void writeSynchronousBatch(List objects) {
        try {
            WriteApiBlocking writeApi = client.getWriteApiBlocking();
            writeApi.writeMeasurements(bucket, org, WritePrecision.S, objects);
            log.info("---------------同步批量写入InfluxDB成功");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 写入数据-异步
     * <p>
     * 以秒为单位：WritePrecision.S
     * </p>
     *
     * @param object
     */
    public void writeAsynchronous(Object object) {
        try {
            WriteApi writeApi = client.makeWriteApi();
            writeApi.writeMeasurement(bucket, org, WritePrecision.S, object);
            log.info("---------------异步写入InfluxDB成功");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 批量写入数据-异步
     * <p>
     * 以秒为单位：WritePrecision.S
     * </p>
     *
     * @param objects
     */
    public void writeAsynchronousBatch(List objects) {
        try {
            WriteApi writeApi = client.makeWriteApi();
            writeApi.writeMeasurements(bucket, org, WritePrecision.S, objects);
            log.info("---------------异步批量写入InfluxDB成功");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 读取数据
     *
     * @param fluxQuery Flux查询语句
     * @return
     */
    public List<FluxTable> queryFluxTable(String fluxQuery) {
        return client.getQueryApi().query(fluxQuery, org);
    }

    /**
     * 读取最新一条数据
     *
     * @return
     */
    public List<FluxTable> queryLastData() {
        Flux flux = Flux
                .from(bucket)
                .range(-7L, ChronoUnit.DAYS)
                .last();
        /*Flux flux = Flux.from(bucket)
                .expression("range(start: 0)")
                .first();*/
        log.info("\n" + flux.toString());
        return queryFluxTable(flux.toString());
    }
}
