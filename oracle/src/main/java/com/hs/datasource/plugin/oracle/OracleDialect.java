package com.hs.datasource.plugin.oracle;

import com.hs.datasource.common.utils.StringPool;
import com.hs.datasource.common.utils.StringUtil;
import com.hs.datasource.plugin.rdbms.RdbmsConst;
import com.hs.datasource.plugin.rdbms.RdbmsDialect;

public class OracleDialect implements RdbmsDialect {

    @Override
    public boolean isSupportCatalog() {
        return false;
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
        return null;
    }

    @Override
    public String defaultCase(String content) {
        return content.toUpperCase();
    }

    @Override
    public String escapeLeftCharacter() {
        return StringPool.QUOTE;
    }

    @Override
    public String escapeRightCharacter() {
        return escapeLeftCharacter();
    }

    @Override
    public int getIdentifierLength() {
        return 30;
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
        return "VARCHAR2(" + length + ")";
    }

    @Override
    public String getLongType(int precision) {
        return "NUMBER(" + precision + ")";
    }

    @Override
    public String getDoubleType(int precision, int scale) {
        return "NUMBER(" + precision + "," + scale + ")";
    }

    @Override
    public String getBooleanType() {
        return "CHAR(1)";
    }

    @Override
    public String getDateType(int precision, int scale) {
        return "DATE";
    }

    @Override
    public String getTimeType(int precision, int scale) {
        return getDateType(precision, scale);
    }

    @Override
    public String getTimestampType(int precision, int scale) {
        if (precision > 0 && precision <= 9) {
            return "TIMESTAMP(" + precision + ")";
        } else if (precision > 9) {
            return "TIMESTAMP(9)";
        } else {
            return "TIMESTAMP";
        }
    }

    @Override
    public String getByteType(int length) {
        return "BLOB";
    }

    @Override
    public String uuid() {
        return "SYS_GUID()";
    }

    @Override
    public String sysDate() {
        return "SYSDATE";
    }

    @Override
    public String sysTimestamp() {
        return sysTimestamp(3);
    }

    @Override
    public String sysTimestamp(int scale) {
        return "SYSTIMESTAMP(" + scale + ")";
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long limit) {
        limit = (offset >= 1) ? (offset + limit) : limit;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM (")
                .append(StringPool.SPACE).append(originalSql)
                .append(StringPool.SPACE).append(") TMP WHERE ROWNUM")
                .append(StringPool.SPACE).append(StringPool.LEFT_CHEV).append(StringPool.EQUALS)
                .append(StringPool.SPACE).append(limit).append(StringPool.RIGHT_BRACKET)
                .append(StringPool.SPACE).append("WHERE ROW_ID")
                .append(StringPool.SPACE).append(StringPool.RIGHT_CHEV)
                .append(StringPool.SPACE).append(offset);
        return builder.toString();
    }

    @Override
    public String buildQueryCountTriggerSql(String catalog, String schema, String trigger) {
        String s = checkAndGetSchema(schema).toString();
        StringBuilder where = new StringBuilder();
        if (StringUtil.isNotBlank(s)) {
            where.append("OWNER").append(StringPool.EQUALS)
                    .append(StringPool.SINGLE_QUOTE).append(s)
                    .append(StringPool.SINGLE_QUOTE)
                    .append(StringPool.SPACE).append(StringPool.AND.toUpperCase()).append(StringPool.SPACE);
        }
        where.append("TRIGGER_NAME").append(StringPool.EQUALS)
                .append(StringPool.SINGLE_QUOTE).append(trigger)
                .append(StringPool.SINGLE_QUOTE);
        return String.format(RdbmsConst.QUERY_SQL_TEMPLATE, "COUNT(1)", "ALL_TRIGGERS", where, "OWNER,TRIGGER_NAME");
    }

    @Override
    public String buildCreateTriggerSql(String catalog, String schema, String trigger, String table,
                                        String insert, String update, String delete) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR REPLACE TRIGGER")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("AFTER INSERT OR UPDATE OR DELETE ON")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("FOR EACH ROW").append(StringPool.NEWLINE);
        builder.append("DECLARE").append(StringPool.NEWLINE);
        builder.append("BEGIN").append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("IF INSERTING THEN").append(StringPool.NEWLINE)
                .append(StringPool.TAB).append(StringPool.TAB).append(insert).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("ELSIF UPDATING THEN").append(StringPool.NEWLINE)
                .append(StringPool.TAB).append(StringPool.TAB).append(update).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("ELSIF DELETING THEN").append(StringPool.NEWLINE)
                .append(StringPool.TAB).append(StringPool.TAB).append(delete).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("END IF;").append(StringPool.NEWLINE)
                .append("END;");
        return builder.toString();
    }

    @Override
    public String buildCreateInsertTriggerSql(String catalog, String schema, String trigger, String table, String insert) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR REPLACE TRIGGER")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("AFTER INSERT ON")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("FOR EACH ROW").append(StringPool.NEWLINE);
        builder.append("DECLARE").append(StringPool.NEWLINE);
        builder.append("BEGIN").append(StringPool.NEWLINE)
                .append(StringPool.TAB).append(insert).append(StringPool.NEWLINE)
                .append("END;");
        return builder.toString();
    }

    @Override
    public String buildCreateUpdateTriggerSql(String catalog, String schema, String trigger, String table, String update) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR REPLACE TRIGGER")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("AFTER UPDATE ON")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("FOR EACH ROW").append(StringPool.NEWLINE);
        builder.append("DECLARE").append(StringPool.NEWLINE);
        builder.append("BEGIN").append(StringPool.NEWLINE)
                .append(StringPool.TAB).append(update).append(StringPool.NEWLINE)
                .append("END;");
        return builder.toString();
    }

    @Override
    public String buildCreateDeleteTriggerSql(String catalog, String schema, String trigger, String table, String delete) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE OR REPLACE TRIGGER")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, trigger)).append(StringPool.NEWLINE)
                .append(StringPool.TAB).append("AFTER DELETE ON")
                .append(StringPool.SPACE).append(escapeObject(catalog, schema, table))
                .append(StringPool.SPACE).append("FOR EACH ROW").append(StringPool.NEWLINE);
        builder.append("DECLARE").append(StringPool.NEWLINE);
        builder.append("BEGIN").append(StringPool.NEWLINE)
                .append(StringPool.TAB).append(delete).append(StringPool.NEWLINE)
                .append("END;");
        return builder.toString();
    }

    @Override
    public String getValidSql() {
        return "SELECT 1 FROM DUAL";
    }
}
