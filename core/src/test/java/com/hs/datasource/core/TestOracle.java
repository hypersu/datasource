package com.hs.datasource.core;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.utils.JsonUtil;

public class TestOracle {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.newInstance("oracle.json");
        ObjectNode schema = JsonUtil.createObjectNode();
        schema.put("schema", "TEST");
        MetaDataCollector.collect(dataSource, schema);
    }
}
