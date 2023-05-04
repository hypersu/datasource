package com.hs.datasource.plugin.oracle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSourceKey;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.plugin.rdbms.RdbmsDriver;
import com.hs.datasource.plugin.rdbms.RdbmsErrorCode;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static com.hs.datasource.plugin.oracle.OracleConst.SERVICE_MODE;
import static com.hs.datasource.plugin.oracle.OracleConst.SID_MODE;

public class OracleDriver implements RdbmsDriver {
    private static final String SID_URL_FORMAT = "jdbc:oracle:thin:@%s:%s:%s";
    private static final String SERVICE_URL_FORMAT = "jdbc:oracle:thin:@%s:%s/%s";

    @Override
    public String getUrl(ObjectNode config) {
        JsonNode attributes = config.get(DataSourceKey.ATTRIBUTES);
        if (attributes.isArray()) {
            throw CommonException.asException(
                    RdbmsErrorCode.ILLEGAL_TYPE,
                    "attributes应该为数组类型");
        }
        String database = attributes.get(OracleKey.DATABASE).asText();
        String mode = attributes.get(OracleKey.MODE).asText();
        String host = attributes.get(OracleKey.HOST).asText();
        int port = attributes.get(OracleKey.PORT).asInt();
        if (SERVICE_MODE.equals(mode)) {
            return String.format(SERVICE_URL_FORMAT, host, port, database);
        } else if (SID_MODE.equals(mode)) {
            return String.format(SID_URL_FORMAT, host, port, database);
        } else {
            throw CommonException.asException(
                    RdbmsErrorCode.ILLEGAL_VALUE,
                    "不支持的参数值,Oracle插件只支持sid/service两种方式.");
        }
    }

    @Override
    public Properties getProperties(ObjectNode config) {
        JsonNode attributes = config.get(DataSourceKey.ATTRIBUTES);
        if (!attributes.isObject()) {
            throw CommonException.asException(
                    RdbmsErrorCode.ILLEGAL_TYPE,
                    "properties应该为对象类型");
        }
        String user = attributes.get(OracleKey.USER).asText();
        String password = attributes.get(OracleKey.PASSWORD).asText();
        Properties properties = new Properties();
        properties.put(OracleKey.USER, user);
        properties.put(OracleKey.PASSWORD, password);
        ObjectNode propertyObject = (ObjectNode) attributes.get(OracleKey.PROPERTIES);
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