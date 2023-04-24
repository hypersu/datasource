package com.hs.datasource.core.utils;

import com.hs.datasource.common.DataSource;
import com.hs.datasource.common.DataSourceErrorCode;
import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.common.utils.StringUtil;
import com.hs.datasource.core.JarLoader;
import com.hs.datasource.core.Plugins;

import java.util.HashMap;
import java.util.Map;

public class LoadUtil {
    private static final Map<String, JarLoader> CLASS_LOADER_MAP = new HashMap<>();

    public static synchronized JarLoader getClassLoader(String pluginName) {
        JarLoader loader = CLASS_LOADER_MAP.get(pluginName);
        if (null == loader) {
            String path = Plugins.getPath(pluginName);
            if (StringUtil.isBlank(path)) {
                throw CommonException.asException(
                        DataSourceErrorCode.RUNTIME_EXCEPTION,
                        String.format("路径[%s]非法", path));
            }
            loader = new JarLoader(new String[]{path});
            CLASS_LOADER_MAP.put(pluginName, loader);
        }
        return loader;
    }

    public static synchronized Class<? extends DataSource> loadPluginClass(String pluginName, String className) {
        JarLoader loader = getClassLoader(pluginName);
        try {
            return (Class<? extends DataSource>) loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw CommonException.asException(
                    DataSourceErrorCode.RUNTIME_EXCEPTION,
                    "加载组件类异常.", e);
        }
    }

    public static DataSource loadPlugin(String pluginName, String className) {
        Class<? extends DataSource> cls = loadPluginClass(pluginName, className);
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw CommonException.asException(
                    DataSourceErrorCode.RUNTIME_EXCEPTION,
                    "加载组件异常.", e);
        }
    }
}
