package com.hypersu.datasource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.utils.JsonUtil;
import com.hs.datasource.core.DataSources;
import com.hs.datasource.core.MetaDataCollector;

public class TestMysql {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.newInstance("mysql.json");
        ObjectNode catalog = JsonUtil.createObjectNode();
        catalog.put("catalog", "test");
        MetaDataCollector.collect(dataSource, catalog);
    }
}
