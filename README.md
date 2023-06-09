# DataSource ![Build Status](https://img.shields.io/hexpm/l/plug?color=green)
- 背景说明
- 安装步骤
- 使用方法
- 功能规划
- 支持列表

## 背景说明
DataSource项目为解决数据源客户端SDK版本冲突；不支持横向、竖向扩展；元数据无法扩展等问题。
## 安装步骤
    make clean install
## 使用方法
1. 解压assembly打包模块中target/datasource.tar.gz文件。
2. 配置DATASOURCE_HOME环境变量，一般为datasource.tar.gz的安装目录。
3. 项目引用core依赖。
   ```
   <dependency>
     <artifactId>datasource</artifactId>
     <groupId>com.hs.datasource</groupId>
     <version>1.0.0</version>
   </dependency>
   ```
4. 生成数据源配置文件，放置在DATASOURCE_HOME/data/conf/目录下，命名格式为“*.json”。
   ```
   {
     "name": "mysql",
     "attributes": {
       "user": "root",
       "password": "123456",
       "host": "localhost",
       "port": 3306,
       "database": "test",
       "properties": {}
     }
   }
   ```
5. 使用DataSources API创建数据源。
   ```
   public class Test {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.newInstance("testMysql.json");
        MetaDataCollector.collect(dataSource);
    }
   }
   ```
6. 注意：Windows NTFS系统会对文本文件名进行校验，如果元数据中包含\ / : * ? " < > |则元数据采集会被终止。
## 功能规划
1. 支持多种数据源，关系型、非关系型、文件服务器等。
3. 支持元数据采集、持久化，可配置的扩展元数据类型（已实现）。
3. 支持各种数据源的方言特性，用于各个数据源元数据转化、SQL语法支持（部分实现）。 
4. 支持数据预览功能（部分实现）。
5. 部署免安装，打包成tar压缩文件（已实现）。
## 支持列表
|   类型   |    数据源    |支持|
|:------:|:---------:|:--:|
| 关系型数据库 |  Oracle   |√|
|        |   Mysql   |√|
|        | SqlServer |√|
|  非关系型  |MongoDB|x|
|        | InfluxDB|x|