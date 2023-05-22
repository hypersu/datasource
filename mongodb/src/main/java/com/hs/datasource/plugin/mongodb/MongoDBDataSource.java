package com.hs.datasource.plugin.mongodb;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.ConnectionAdapter;
import com.hs.datasource.common.DataSource;

public class MongoDBDataSource implements DataSource {

    @Override
    public void setConfig(ObjectNode config) {

    }

    @Override
    public ObjectNode getConfig() {
        return null;
    }

    @Override
    public ConnectionAdapter getConnection() {
        return null;
    }

}
