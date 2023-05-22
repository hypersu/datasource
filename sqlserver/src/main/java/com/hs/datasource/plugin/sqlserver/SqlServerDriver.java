package com.hs.datasource.plugin.sqlserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.plugin.rdbms.RdbmsDriver;
import com.hs.datasource.plugin.rdbms.RdbmsErrorCode;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class SqlServerDriver implements RdbmsDriver {
    private static final String URL_FORMAT = "jdbc:sqlserver://%s:%s;databaseName=%s";

    @Override
    public String getUrl(ObjectNode config) {
        JsonNode attributes = config.get(DataSourceKey.ATTRIBUTES);
        if (attributes.isArray()) {
            throw CommonException.asException(
                    RdbmsErrorCode.ILLEGAL_TYPE,
                    "attributes应该为数组类型");
        }
        String database = attributes.get(SqlServerKey.DATABASE).asText();
        String host = attributes.get(SqlServerKey.HOST).asText();
        int port = attributes.get(SqlServerKey.PORT).asInt();
        return String.format(URL_FORMAT, host, port, database);
    }

    @Override
    public Properties getProperties(ObjectNode config) {
        JsonNode attributes = config.get(DataSourceKey.ATTRIBUTES);
        if (!attributes.isObject()) {
            throw CommonException.asException(
                    RdbmsErrorCode.ILLEGAL_TYPE,
                    "properties应该为对象类型");
        }
        String user = attributes.get(SqlServerKey.USER).asText();
        String password = attributes.get(SqlServerKey.PASSWORD).asText();
        Properties properties = new Properties();
        properties.put(SqlServerKey.USER, user);
        properties.put(SqlServerKey.PASSWORD, password);
        ObjectNode propertyObject = (ObjectNode) attributes.get(SqlServerKey.PROPERTIES);
        if (propertyObject == null) {
            return properties;
        }
        Iterator<Map.Entry<String, JsonNode>> iterator = propertyObject.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            properties.put(entry.getKey(), entry.getValue().asText());
        }
        return properties;
    }
}