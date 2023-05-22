package com.hs.datasource.plugin.sqlserver;

import com.hs.datasource.common.utils.StringPool;
import com.hs.datasource.plugin.rdbms.RdbmsConst;
import com.hs.datasource.plugin.rdbms.RdbmsDialect;

public class SqlServerDialect implements RdbmsDialect {

    @Override
    public boolean isSupportCatalog() {
        return true;
    }

    @Override
    public boolean isSupportSchema() {
        return true;
    }

    @Override
    public String defaultCatalog() {
        return null;
    }

    @Override
    public String defaultSchema() {
        return "dbo";
    }

    @Override
    public String defaultCase(String content) {
        return content.toLowerCase();
    }

    @Override
    public String escapeLeftCharacter() {
        return StringPool.LEFT_SQ_BRACKET;
    }

    @Override
    public String escapeRightCharacter() {
        return StringPool.RIGHT_SQ_BRACKET;
    }

    @Override
    public int getIdentifierLength() {
        return 128;
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
            return "char(1)";
        }
        return "varchar(" + length + ")";
    }

    @Override
    public String uuid() {
        return "NEWID()";
    }

    @Override
    public String sysDate() {
        return "GETDATE()";
    }

    @Override
    public String sysTimestamp() {
        return sysDate();
    }

    @Override
    public String sysTimestamp(int scale) {
        return sysDate();
    }

    @Override
    public String getLongType(int precision) {
        return "int(" + precision + ")";
    }

    @Override
    public String getDoubleType(int precision, int scale) {
        return "decimal(" + precision + "," + scale + ")";
    }

    @Override
    public String getBooleanType() {
        return "bit";
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
        if (scale > 0 && scale <= 7) {
            return "datetime2(" + scale + ")";
        } else if (scale > 7) {
            return "datetime2(7)";
        } else {
            return "datetime";
        }
    }

    @Override
    public String getByteType(int length) {
        return "binary(" + length + ")";
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long limit) {
        StringBuilder builder = new StringBuilder();
        builder.append(originalSql)
                .append(StringPool.SPACE).append("OFFSET")
                .append(StringPool.SPACE).append(offset)
                .append(StringPool.SPACE).append("ROWS FETCH NEXT")
                .append(StringPool.SPACE).append(limit)
                .append(StringPool.SPACE).append("ROWS ONLY");
        return builder.toString();
    }

    @Override
    public String buildQueryCountTriggerSql(String catalog, String schema, String trigger) {
        StringBuilder where = new StringBuilder();
        where.append("name").append(StringPool.EQUALS)
                .append(StringPool.SINGLE_QUOTE).append(trigger).append(StringPool.SINGLE_QUOTE);
        return String.format(RdbmsConst.QUERY_SQL_TEMPLATE, "COUNT(1)", "sys.triggers", where, "name");
    }

    @Override
    public String buildCreateTriggerSql(String catalog, String schema, String trigger, String table, String insert, String update, String delete) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR ALTER TRIGGER").append(StringPool.SPACE).append(escapeObject(schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("ON").append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("AFTER INSERT,UPDATE,DELETE").append(StringPool.NEWLINE);
        builder.append("AS").append(StringPool.NEWLINE);
        // 插入
        builder.append(insert).append(StringPool.NEWLINE);
        // 删除
        builder.append(delete).append(StringPool.NEWLINE);
        return builder.toString();
    }

    @Override
    public String buildCreateInsertTriggerSql(String catalog, String schema, String trigger, String table, String insert) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR ALTER TRIGGER").append(StringPool.SPACE).append(escapeObject(schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("ON").append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("AFTER INSERT").append(StringPool.NEWLINE);
        builder.append("AS").append(StringPool.NEWLINE);
        builder.append(insert).append(StringPool.NEWLINE);
        return builder.toString();
    }

    @Override
    public String buildCreateUpdateTriggerSql(String catalog, String schema, String trigger, String table, String update) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR ALTER TRIGGER").append(StringPool.SPACE).append(escapeObject(schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("ON").append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("AFTER UPDATE").append(StringPool.NEWLINE);
        builder.append("AS").append(StringPool.NEWLINE);
        builder.append(update).append(StringPool.NEWLINE);
        return builder.toString();
    }

    @Override
    public String buildCreateDeleteTriggerSql(String catalog, String schema, String trigger, String table, String delete) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR ALTER TRIGGER").append(StringPool.SPACE).append(escapeObject(schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("ON").append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("AFTER DELETE").append(StringPool.NEWLINE);
        builder.append("AS").append(StringPool.NEWLINE);
        builder.append(delete).append(StringPool.NEWLINE);
        return builder.toString();
    }

    @Override
    public String buildDropTriggerSql(String catalog, String schema, String trigger) {
        String escapeTrigger = escapeObject(schema, trigger);
        return String.format(RdbmsConst.DROP_TRIGGER_SQL_TEMPLATE, escapeTrigger);
    }

    @Override
    public String getValidSql() {
        return "SELECT 1";
    }
}
