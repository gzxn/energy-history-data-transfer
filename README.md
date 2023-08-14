# energy-history-data-transfer

Mysql表数据转移到InfluxDB中，版本：Mysql5.7、InfluxDB2.6.1

#环境

> 核心技术栈

| 软件名称  | 描述 | 版本
|---|---|---
|JDK | Java环境 | 1.8
|Spring Boot | 开发框架 | 2.3.1.RELEASE
|InfluxDB | 时序数据库 | 2.6.1
|Flux-DSL | Flux查询工具 | 2.6.1与InfluxDB一致
|MySQL | 数据库 | 5.7
|Mybatis | 持久层框架 | 2.1.3

# 开发

> 新增MySQL数据库其他表进行转移操作步骤

1. 在entity包中新增MySQL表实体类和InfluxDB的Measurement实体类。
2. 在mode包中新增一个模板类，继承SyncData并实现Runnable接口，Runnable接口非必须，你也可以用线程池提交一个线程操作。
3. 在config包的ScanDataConfig类中，新增一个Bean，将InfluxDbBean、TxtFileBean和ScaHistoryDataDao注入到你的模板类中，你也可以编写其他的Dao进行注入。
4. 在scheduled包中新建一个定时任务类，然后写一个定时任务方法调用模板类的sync方法即可。
5. 在application-dev.properties中，新增一个配置txt文件的路径，txt文件保存你将要从MySQL数据库表开始转移数据的具体自增主键ID，然后在TxtFileConfig类中添加一个path和Bean。
