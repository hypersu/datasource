package com.hs.datasource.core.data;

import java.io.File;
import java.io.FileFilter;

public class MetaDataTreeFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        if (pathname.isDirectory()) {
            return true;
        }
        return false;
    }

}
