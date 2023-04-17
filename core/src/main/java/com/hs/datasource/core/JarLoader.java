package com.hs.datasource.core;

import com.hs.datasource.common.DataSourceErrorCode;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.common.utils.StringUtil;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class JarLoader extends URLClassLoader {

    public JarLoader(String[] paths) {
        this(paths, JarLoader.class.getClassLoader());
    }

    public JarLoader(String[] paths, ClassLoader parent) {
        super(JarLoader.getURLs(paths), parent);
    }

    private static URL[] getURLs(String[] paths) {
        if (null == paths || 0 == paths.length) {
            throw CommonException.asException(
                    DataSourceErrorCode.RUNTIME_EXCEPTION, "jar包路径不能为空.");
        }
        List<String> dirs = new ArrayList<>();
        for (String path : paths) {
            dirs.add(path);
            JarLoader.collectDirs(path, dirs);
        }
        List<URL> urls = new ArrayList<>();
        for (String path : dirs) {
            urls.addAll(JarLoader.doGetURLs(path));
        }

        return urls.toArray(new URL[0]);
    }

    private static void collectDirs(String path, List<String> collector) {
        if (null == path || StringUtil.isBlank(path)) {
            return;
        }

        File current = new File(path);
        if (!current.exists() || !current.isDirectory()) {
            return;
        }

        for (File child : current.listFiles()) {
            if (!child.isDirectory()) {
                continue;
            }

            collector.add(child.getAbsolutePath());
            collectDirs(child.getAbsolutePath(), collector);
        }
    }

    private static List<URL> doGetURLs(final String path) {
        if (StringUtil.isBlank(path)) {
            throw CommonException.asException(
                    DataSourceErrorCode.RUNTIME_EXCEPTION, "jar包路径不能为空.");
        }

        File jarPath = new File(path);
        if (!jarPath.exists() || !jarPath.isDirectory()) {
            throw CommonException.asException(
                    DataSourceErrorCode.RUNTIME_EXCEPTION, "jar包路径必须存在且为目录.");
        }

        /* set filter */
        FileFilter jarFilter = (pathname) -> pathname.getName().endsWith(".jar");

        /* iterate all jar */
        File[] allJars = new File(path).listFiles(jarFilter);
        List<URL> jarURLs = new ArrayList<>(allJars.length);

        for (int i = 0; i < allJars.length; i++) {
            try {
                jarURLs.add(allJars[i].toURI().toURL());
            } catch (Exception e) {
                throw CommonException.asException(
                        DataSourceErrorCode.CLASS_EXCEPTION,
                        "系统加载jar包出错", e);
            }
        }

        return jarURLs;
    }
}
