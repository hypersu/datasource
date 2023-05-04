package com.hs.datasource.plugin.rdbms;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Properties;

public interface RdbmsDriver {

    String getUrl(ObjectNode config);

    Properties getProperties(ObjectNode config);
}
