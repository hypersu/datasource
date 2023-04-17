package com.hs.datasource.rdbms;

import com.hs.datasource.common.utils.StringPool;
import com.hs.datasource.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface RdbmsDialect {

    boolean isSupportCatalog();

    boolean isSupportSchema();

    String defaultCatalog();

    String defaultSchema();

    default StringBuilder checkAndGetCatalog(String catalog) {
        StringBuilder builder = new StringBuilder();
        if (isSupportCatalog()) {
            if (StringUtil.isNotBlank(catalog)) {
                builder.append(escapeLeftCharacter())
                        .append(catalog)
                        .append(escapeRightCharacter())
                        .append(StringPool.DOT);
            } else if (StringUtil.isNotBlank(defaultCatalog())) {
                builder.append(escapeLeftCharacter())
                        .append(defaultCatalog())
                        .append(escapeRightCharacter())
                        .append(StringPool.DOT);
            }
        }
        return builder;
    }

    default StringBuilder checkAndGetCatalog() {
        return checkAndGetCatalog(null);
    }

    default StringBuilder checkAndGetSchema(String schema) {
        StringBuilder builder = new StringBuilder();
        if (isSupportSchema()) {
            if (StringUtil.isNotBlank(schema)) {
                builder.append(escapeLeftCharacter())
                        .append(schema)
                        .append(escapeRightCharacter())
                        .append(StringPool.DOT);
            } else if (StringUtil.isNotBlank(defaultSchema())) {
                builder.append(escapeLeftCharacter())
                        .append(defaultSchema())
                        .append(escapeRightCharacter())
                        .append(StringPool.DOT);
            }
        }
        return builder;
    }

    default StringBuilder checkAndGetSchema() {
        return checkAndGetSchema(null);
    }

    // default upper
    String defaultCase(String content);

    String escapeLeftCharacter();

    String escapeRightCharacter();

    default String escapeObject(String object) {
        StringBuilder builder = new StringBuilder();
        builder.append(escapeLeftCharacter())
                .append(object)
                .append(escapeRightCharacter());
        return builder.toString();
    }

    default List<String> escapeList(List<String> objects) {
        return objects.stream()
                .map(object -> escapeObject(object))
                .collect(Collectors.toList());
    }

    default String escapeObject(String schema, String object) {
        StringBuilder builder = checkAndGetSchema(schema);
        builder.append(escapeObject(object));
        return builder.toString();
    }

    default String escapeObject(String catalog, String schema, String object) {
        StringBuilder builder = checkAndGetCatalog(catalog);
        builder.append(checkAndGetSchema(schema))
                .append(escapeObject(object));
        return builder.toString();
    }

    int getIdentifierLength();

    String getTypeName(int dataType, int length, int precision, int scale);

    String getTypeNameByClassName(String className, int length, int precision, int scale);

    String getStringType(int length);

    String getLongType(int precision);

    String getDoubleType(int precision, int scale);

    String getBooleanType();

    String getDateType(int precision, int scale);

    String getTimeType(int precision, int scale);

    String getTimestampType(int precision, int scale);

    String getByteType(int length);

    String uuid();

    String sysDate();

    String sysTimestamp();

    String sysTimestamp(int scale);

    default String join(List<String> objects, String holder) {
        return StringUtil.join(objects, holder);
    }

    default String joinWithComma(List<String> objects) {
        return StringUtil.join(objects, StringPool.COMMA + StringPool.SPACE);
    }

    default String joinWithAnd(List<String> objects) {
        return StringUtil.join(objects, StringPool.SPACE + StringPool.AND.toUpperCase()
                + StringPool.SPACE);
    }

    default List<String> mergeWithEquals(List<String> columns, List<String> holders) {
        int columnSize = columns.size();
        List<String> escapeColumns = new ArrayList<>(columnSize);
        for (int i = 0; i < columnSize; i++) {
            String column = columns.get(i);
            String holder = holders.get(i);
            escapeColumns.add(escapeObject(column)
                    + StringPool.SPACE
                    + StringPool.EQUALS
                    + StringPool.SPACE
                    + holder);
        }
        return escapeColumns;
    }

    // 分页语句
    String buildPaginationSql(String originalSql, long offset, long limit);

    String buildQueryCountTriggerSql(String catalog, String schema, String trigger);

    // 触发器
    String buildCreateTriggerSql(String catalog, String schema, String trigger, String table,
                                 String insert, String update, String delete);

    String buildCreateInsertTriggerSql(String catalog, String schema, String trigger,
                                       String table, String insert);

    String buildCreateUpdateTriggerSql(String catalog, String schema, String trigger,
                                       String table, String update);

    String buildCreateDeleteTriggerSql(String catalog, String schema, String trigger,
                                       String table, String delete);

    default String buildCreateTableSql(String catalog, String schema, String table, String column) {
        String escapeTable = escapeObject(catalog, schema, table);
        return String.format(RdbmsConst.CREATE_TABLE_SQL_TEMPLATE, escapeTable, column);
    }

    default String buildDropTriggerSql(String catalog, String schema, String trigger) {
        String escapeTrigger = escapeObject(catalog, schema, trigger);
        return String.format(RdbmsConst.DROP_TRIGGER_SQL_TEMPLATE, escapeTrigger);
    }

    default String buildDropTableSql(String catalog, String schema, String table) {
        String escapeTable = escapeObject(catalog, schema, table);
        return String.format(RdbmsConst.DROP_TABLE_SQL_TEMPLATE, escapeTable);
    }

    default String buildQuerySql(String catalog, String schema, String table) {
        String escapeTable = escapeObject(catalog, schema, table);
        return String.format(
                RdbmsConst.QUERY_SQL_TEMPLATE_WITHOUT_WHERE_AND_ORDER,
                StringPool.STAR, escapeTable);
    }

    default String buildQuerySql(String catalog, String schema, String table,
                                 List<String> columns, String whereHolder) {
        String escapeTable = escapeObject(catalog, schema, table);
        List<String> escapeColumns = columns.stream()
                .map(column -> escapeObject(column))
                .collect(Collectors.toList());
        if (whereHolder == null) {
            return String.format(
                    RdbmsConst.QUERY_SQL_TEMPLATE_WITHOUT_WHERE_AND_ORDER,
                    joinWithComma(escapeColumns), escapeTable);
        } else {
            return String.format(
                    RdbmsConst.QUERY_SQL_TEMPLATE_WITHOUT_ORDER,
                    joinWithComma(escapeColumns), escapeTable, whereHolder);
        }
    }

    default String buildQuerySql(String catalog, String schema, String table,
                                 List<String> columns, List<String> whereColumns,
                                 List<String> whereHolders, List<String> orderColumns) {
        String escapeTable = escapeObject(catalog, schema, table);
        String columnStr;
        if (columns == null) {
            columnStr = StringPool.STAR;
        } else {
            List<String> escapeColumns = columns.stream()
                    .map(column -> escapeObject(column))
                    .collect(Collectors.toList());
            columnStr = joinWithComma(escapeColumns);
        }
        int whereColumnSize = whereColumns.size();
        List<String> escapeWhereColumns = new ArrayList<>(whereColumnSize);
        for (int i = 0; i < whereColumnSize; i++) {
            String column = whereColumns.get(i);
            String holder = whereHolders.get(i);
            escapeWhereColumns.add(escapeObject(column)
                    + StringPool.SPACE
                    + StringPool.EQUALS
                    + StringPool.SPACE
                    + holder);
        }
        if (orderColumns == null) {
            return String.format(RdbmsConst.QUERY_SQL_TEMPLATE_WITHOUT_ORDER,
                    columnStr, escapeTable,
                    joinWithAnd(escapeWhereColumns));
        } else {
            List<String> escapeOrderColumns = orderColumns.stream()
                    .map(orderColumn -> escapeObject(orderColumn))
                    .collect(Collectors.toList());
            return String.format(RdbmsConst.QUERY_SQL_TEMPLATE,
                    columnStr, escapeTable,
                    joinWithAnd(escapeWhereColumns),
                    joinWithComma(escapeOrderColumns));
        }
    }

    default String buildUpdateSql(String catalog, String schema, String table,
                                  List<String> columns, List<String> holders,
                                  List<String> whereColumns, List<String> whereHolders) {
        String escapeTable = escapeObject(catalog, schema, table);
        List<String> escapeColumns = mergeWithEquals(columns, holders);
        List<String> escapeWhereColumns = mergeWithEquals(whereColumns, whereHolders);
        return String.format(RdbmsConst.UPDATE_SQL_TEMPLATE, escapeTable,
                joinWithComma(escapeColumns), joinWithAnd(escapeWhereColumns));
    }

    default String buildInsertSql(String catalog, String schema, String table,
                                  List<String> columns, List<String> holders) {
        String escapeTable = escapeObject(catalog, schema, table);
        List<String> escapeColumns = escapeList(columns);
        return String.format(RdbmsConst.INSERT_SQL_TEMPLATE, escapeTable,
                joinWithComma(escapeColumns), joinWithComma(holders));
    }

    default String buildDeleteSql(String catalog, String schema, String table,
                                  List<String> whereColumns, List<String> whereHolders) {
        String escapeTable = escapeObject(catalog, schema, table);
        List<String> escapeColumns = mergeWithEquals(whereColumns, whereHolders);
        return String.format(RdbmsConst.DELETE_SQL_TEMPLATE,
                escapeTable, joinWithAnd(escapeColumns));
    }

    default String buildPreparedQuerySql(String catalog, String schema, String table, List<String> columns,
                                         List<String> whereColumns, List<String> orderColumns) {
        int whereColumnSize = whereColumns.size();
        List<String> whereHolders = new ArrayList<>(whereColumnSize);
        for (int i = 0; i < whereColumnSize; i++) {
            whereHolders.add(StringPool.QUESTION_MARK);
        }
        return buildQuerySql(catalog, schema, table,
                columns, whereColumns, whereHolders, orderColumns);
    }

    default String buildPreparedInsertSql(String catalog, String schema,
                                          String table, List<String> columns) {
        int size = columns.size();
        List<String> holders = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            holders.add(StringPool.QUESTION_MARK);
        }
        return buildInsertSql(catalog, schema, table, columns, holders);
    }

    default String buildPreparedUpdateSql(String catalog, String schema,
                                          String table, List<String> columns, List<String> primaryKeys) {
        String escapeTable = escapeObject(catalog, schema, table);
        List<String> set = new ArrayList<>(columns.size());
        columns.forEach(column -> {
            set.add(escapeObject(column)
                    + StringPool.SPACE
                    + StringPool.EQUALS
                    + StringPool.SPACE
                    + StringPool.QUESTION_MARK);
        });
        List<String> where = new ArrayList<>(primaryKeys.size());
        primaryKeys.forEach(primaryKey -> {
            where.add(escapeObject(primaryKey)
                    + StringPool.SPACE
                    + StringPool.EQUALS
                    + StringPool.SPACE
                    + StringPool.QUESTION_MARK);
        });
        return String.format(RdbmsConst.UPDATE_SQL_TEMPLATE,
                escapeTable, joinWithComma(set), joinWithAnd(where));
    }

    default String buildPreparedDeleteSql(String catalog, String schema,
                                          String table, List<String> primaryKeys) {
        String escapeTable = escapeObject(catalog, schema, table);
        List<String> where = new ArrayList<>(primaryKeys.size());
        primaryKeys.forEach(primaryKey -> {
            where.add(escapeObject(primaryKey)
                    + StringPool.SPACE
                    + StringPool.EQUALS
                    + StringPool.SPACE
                    + StringPool.QUESTION_MARK);
        });
        return String.format(RdbmsConst.DELETE_SQL_TEMPLATE,
                escapeTable, joinWithAnd(where));
    }

    default String buildTruncateSql(String catalog, String schema, String table) {
        String escapeTable = escapeObject(catalog, schema, table);
        return String.format(RdbmsConst.TRUNCATE_SQL_TEMPLATE, escapeTable);
    }

    String getValidSql();
}
