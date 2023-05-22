package com.hs.datasource.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.utils.JsonUtil;

public class TestSqlServer {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.newInstance("sqlserver.json");
        ObjectNode catalog = JsonUtil.createObjectNode();
        catalog.put("catalog", "test");
        catalog.put("schema", "dbo");
        MetaDataCollector.collect(dataSource, catalog);
    }
}
