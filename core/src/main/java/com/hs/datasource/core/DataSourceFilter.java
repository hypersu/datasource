package com.hs.datasource.core;

import com.hs.datasource.common.DataSourceConst;

import java.io.File;
import java.io.FileFilter;

public class DataSourceFilter implements FileFilter {
    private static final DataSourceFilter filter = new DataSourceFilter();

    public static DataSourceFilter getInstance() {
        return filter;
    }

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return false;
        }
        String fileName = pathname.getName();
        if (fileName.endsWith(DataSourceConst.FILE_SUFFIX)) {
            return true;
        }
        return false;
    }

}
