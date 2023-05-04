package com.hs.datasource.mysql;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.rdbms.RdbmsDriver;
import com.hs.datasource.rdbms.RdbmsErrorCode;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class MysqlDriver implements RdbmsDriver {
    private static final String URL_FORMAT = "jdbc:mysql://%s:%s/%s";

    @Override
    public String getUrl(ObjectNode config) {
        JsonNode attributes = config.get(DataSourceKey.ATTRIBUTES);
        if (attributes.isArray()) {
            throw CommonException.asException(
                    RdbmsErrorCode.ILLEGAL_TYPE,
                    "attributes应该为数组类型");
        }
        String database = attributes.get(MysqlKey.DATABASE).asText();
        String host = attributes.get(MysqlKey.HOST).asText();
        int port = attributes.get(MysqlKey.PORT).asInt();
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
        String user = attributes.get(MysqlKey.USER).asText();
        String password = attributes.get(MysqlKey.PASSWORD).asText();
        ObjectNode propertyObject = (ObjectNode) attributes.get(MysqlKey.PROPERTIES);
        Properties properties = new Properties();
        properties.put(MysqlKey.USER, user);
        properties.put(MysqlKey.PASSWORD, password);
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
