package com.hs.datasource.common;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface DataSource {
    void setConfig(ObjectNode config);

    ObjectNode getConfig();

    ConnectionAdapter getConnection();
}
