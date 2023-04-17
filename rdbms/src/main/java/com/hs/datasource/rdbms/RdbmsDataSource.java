package com.hs.datasource.rdbms;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.DataSourceKey;

public abstract class RdbmsDataSource implements DataSource {

    public ObjectNode getAttributes(ObjectNode config) {
        if (config == null) {
            return null;
        }
        return (ObjectNode) config.get(DataSourceKey.ATTRIBUTES);
    }

    public abstract RdbmsDialect getDialect();

    public abstract RdbmsDriver getDriver();
}
