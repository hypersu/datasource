package com.hs.datasource.common;

import java.io.File;

public class DataSourceConst {
    /**
     * 目录结构
     * <p>datasource</p>
     * <p>&nbsp;&nbsp;├─conf</p>
     * <p>&nbsp;&nbsp;├─data</p>
     * <p>&nbsp;&nbsp;│&nbsp;&nbsp;├─conf</p>
     * <p>&nbsp;&nbsp;│&nbsp;&nbsp;└─meta</p>
     * <p>&nbsp;&nbsp;├─lib</p>
     * <p>&nbsp;&nbsp;├─logs</p>
     * <p>&nbsp;&nbsp;└─plugin</p>
     */
    public static final String DATASOURCE_HOME = System.getenv("DATASOURCE_HOME");
    public static final String PLUGIN_PATH = DATASOURCE_HOME + File.separator + "plugin";
    public static final String DATA_PATH = DATASOURCE_HOME + File.separator + "data";
    public static final String CONF_PATH = DATA_PATH + File.separator + "conf";
    public static final String META_PATH = DATA_PATH + File.separator + "meta";

    public static final String FILE_SUFFIX = ".json";
}
