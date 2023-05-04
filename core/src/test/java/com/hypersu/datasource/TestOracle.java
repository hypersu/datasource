package com.hypersu.datasource;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.utils.JsonUtil;
import com.hs.datasource.core.DataSources;
import com.hs.datasource.core.MetaDataCollector;

public class TestOracle {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.newInstance("oracle.json");
        ObjectNode schema = JsonUtil.createObjectNode();
        schema.put("schema","TEST");
        MetaDataCollector.collect(dataSource, schema);
    }
}
