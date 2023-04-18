# DataSource ![Build Status](https://img.shields.io/hexpm/l/plug?color=green)
- 背景说明
- 安装步骤
- 使用方法
- 功能规划
- 目录结构

## 背景说明
DataSource项目为解决数据源客户端SDK版本冲突；不支持横向、竖向扩展；元数据无法扩展等问题。
## 安装步骤
    make clean install
## 使用方法
1. 配置DATASOURCE_HOME环境变量，一般为DataSource的安装目录。
2. 项目引用core依赖。
   ```
   <dependency>
     <artifactId>datasource</artifactId>
     <groupId>com.hs.datasource</groupId>
     <version>1.0.0</version>
   </dependency>]
   ```
3. 使用DataSources API创建数据源。
   ```
   public class Test {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.
          newInstance("mysql37.json");

        MetaDataCollector.collect(dataSource);
    }
   }
   ```
## 功能规划
1. 支持多种数据源，关系型、非关系型、文件服务器等。
2. 支持元数据采集、持久化，可配置的扩展元数据类型:winking_face_with_tongue:。
3. 支持数据源间元数据转化:winking_face_with_tongue:。
4. 支持数据预览功能:winking_face_with_tongue:。
