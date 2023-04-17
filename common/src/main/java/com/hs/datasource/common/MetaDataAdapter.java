package com.hs.datasource.common;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface MetaDataAdapter {

    ArrayNode get(String type);

    ArrayNode get(String type, ObjectNode meta);
}
