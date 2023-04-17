package com.hs.datasource.common;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ConnectionAdapter {
    /**
     * 测试连接
     */
    void test();

    /**
     * 获取元数据
     */
    MetaDataAdapter getMetaData();

    /**
     * 预览查询数据
     */
    ArrayNode previewQuery(ObjectNode object);

    /**
     * 关闭连接
     */
    void close();
}
