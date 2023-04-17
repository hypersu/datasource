package com.hs.datasource.mysql;

import com.hs.datasource.common.exception.CommonException;
import com.hs.datasource.common.utils.StringPool;
import com.hs.datasource.common.utils.StringUtil;
import com.hs.datasource.rdbms.RdbmsConst;
import com.hs.datasource.rdbms.RdbmsDialect;
import com.hs.datasource.rdbms.RdbmsErrorCode;

public class MysqlDialect implements RdbmsDialect {
    @Override
    public boolean isSupportCatalog() {
        return true;
    }

    @Override
    public boolean isSupportSchema() {
        return false;
    }

    @Override
    public String defaultCatalog() {
        return "test";
    }

    @Override
    public String defaultSchema() {
        return null;
    }

    @Override
    public String defaultCase(String content) {
        return content.toLowerCase();
    }

    @Override
    public String escapeLeftCharacter() {
        return StringPool.BACKTICK;
    }

    @Override
    public String escapeRightCharacter() {
        return escapeLeftCharacter();
    }

    @Override
    public int getIdentifierLength() {
        return 64;
    }

    @Override
    public String getTypeName(int dataType, int length, int precision, int scale) {
        return null;
    }

    @Override
    public String getTypeNameByClassName(String className, int length, int precision, int scale) {
        return null;
    }

    @Override
    public String getStringType(int length) {
        if (length == 1) {
            return getBooleanType();
        }
        return "varchar(" + length + ")";
    }

    @Override
    public String getLongType(int precision) {
        return "int(" + precision + ")";
    }

    @Override
    public String getDoubleType(int precision, int scale) {
        return "double(" + precision + "," + scale + ")";
    }

    @Override
    public String getBooleanType() {
        return "char(1)";
    }

    @Override
    public String getDateType(int precision, int scale) {
        return "date";
    }

    @Override
    public String getTimeType(int precision, int scale) {
        return "time";
    }

    @Override
    public String getTimestampType(int precision, int scale) {
        if (scale > 0 && scale <= 6) {
            return "datetime(" + scale + ")";
        } else if (scale > 6) {
            return "datetime(6)";
        } else {
            return "datetime";
        }
    }

    @Override
    public String getByteType(int length) {
        return "blob";
    }

    @Override
    public String uuid() {
        return "UUID()";
    }

    @Override
    public String sysDate() {
        return "NOW()";
    }

    @Override
    public String sysTimestamp() {
        return sysTimestamp(3);
    }

    @Override
    public String sysTimestamp(int scale) {
        return "NOW(" + scale + ")";
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long limit) {
        StringBuilder builder = new StringBuilder();
        builder.append(originalSql)
                .append(StringPool.SPACE).append("LIMIT")
                .append(StringPool.SPACE).append(offset)
                .append(StringPool.COMMA).append(StringPool.SPACE).append(limit);
        return builder.toString();
    }

    @Override
    public String buildQueryCountTriggerSql(String catalog, String schema, String trigger) {
        String c = checkAndGetCatalog(catalog).toString();
        String table = escapeObject("information_schema", null, "TRIGGERS");
        StringBuilder where = new StringBuilder();
        if (StringUtil.isNotBlank(c)) {
            where.append("TRIGGER_SCHEMA").append(StringPool.EQUALS)
                    .append(StringPool.SINGLE_QUOTE).append(c)
                    .append(StringPool.SINGLE_QUOTE)
                    .append(StringPool.SPACE).append(StringPool.AND.toUpperCase()).append(StringPool.SPACE);
        }
        where.append("TRIGGER_NAME").append(StringPool.EQUALS)
                .append(StringPool.SINGLE_QUOTE).append(trigger)
                .append(StringPool.SINGLE_QUOTE);
        return String.format(RdbmsConst.QUERY_SQL_TEMPLATE, "COUNT(1)", table, where, "TRIGGER_CATALOG,TRIGGER_SCHEMA,TRIGGER_NAME");
    }

    @Override
    public String buildCreateTriggerSql(String catalog, String schema, String trigger,
                                        String table, String insert, String update, String delete) {
        throw CommonException.asException(
                RdbmsErrorCode.UNSUPPORTED_OPERATION_EXCEPTION,
                "MysqlDialect不支持DML级别的触发器！");
    }

    @Override
    public String buildCreateInsertTriggerSql(String catalog, String schema, String trigger,
                                              String table, String insert) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TRIGGER").append(StringPool.SPACE)
                .append(escapeObject(catalog, schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("AFTER INSERT")
                .append(StringPool.SPACE).append("ON")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("FOR EACH ROW").append(StringPool.NEWLINE);
        builder.append("BEGIN").append(StringPool.NEWLINE);
        builder.append(StringPool.TAB).append(insert).append(StringPool.NEWLINE);
        builder.append("END").append(StringPool.SEMICOLON).append(StringPool.NEWLINE);
        return builder.toString();
    }

    @Override
    public String buildCreateUpdateTriggerSql(String catalog, String schema, String trigger, String table, String update) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TRIGGER").append(StringPool.SPACE)
                .append(escapeObject(catalog, schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("AFTER UPDATE")
                .append(StringPool.SPACE).append("ON")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("FOR EACH ROW").append(StringPool.NEWLINE);
        builder.append("BEGIN").append(StringPool.NEWLINE);
        builder.append(StringPool.TAB).append(update).append(StringPool.NEWLINE);
        builder.append("END").append(StringPool.SEMICOLON).append(StringPool.NEWLINE);
        return builder.toString();
    }

    @Override
    public String buildCreateDeleteTriggerSql(String catalog, String schema, String trigger, String table, String delete) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TRIGGER").append(StringPool.SPACE)
                .append(escapeObject(catalog, schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("AFTER DELETE")
                .append(StringPool.SPACE).append("ON")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("FOR EACH ROW").append(StringPool.NEWLINE);
        builder.append("BEGIN").append(StringPool.NEWLINE);
        builder.append(StringPool.TAB).append(delete).append(StringPool.NEWLINE);
        builder.append("END").append(StringPool.SEMICOLON).append(StringPool.NEWLINE);
        return builder.toString();
    }

    @Override
    public String getValidSql() {
        return "SELECT 1";
    }
}
