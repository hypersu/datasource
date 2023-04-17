package com.hypersu.datasource;

import com.hs.datasource.common.DataSource;
import com.hs.datasource.core.DataSources;
import com.hs.datasource.core.MetaDataCollector;

public class Test {
    public static void main(String[] args) {
        DataSource dataSource = DataSources.newInstance("mysql37.json");

        MetaDataCollector.collect(dataSource);
    }
}
