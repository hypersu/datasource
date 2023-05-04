package com.hs.datasource.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.utils.JsonUtil;

public class TestMysql {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.newInstance("mysql.json");
        ObjectNode catalog = JsonUtil.createObjectNode();
        catalog.put("catalog", "test");
        MetaDataCollector.collect(dataSource, catalog);
    }
}
