package com.hs.datasource.common.utils;


import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginUtil {
    private static final Pattern pattern = Pattern.compile("%\\{\\w+\\}");

    public static boolean isVariable(String content) {
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    public static String getVariableName(String content) {
        return content.substring(2, content.length() - 1);
    }

    public static String getString(String content, ObjectNode object) {
        if (isVariable(content)) {
            String variable = getVariableName(content);
            return object.get(variable).asText();
        } else {
            return content;
        }
    }
}
