package com.hs.datasource.rdbms;

public class RdbmsConst {
    // 创建表语句
    public static final String CREATE_TABLE_SQL_TEMPLATE = "CREATE TABLE %s(%s)";

    // 删除表语句
    public static final String DROP_TABLE_SQL_TEMPLATE = "DROP TABLE %s";

    // 删除触发器语句
    public static final String DROP_TRIGGER_SQL_TEMPLATE = "DROP TRIGGER %s";

    // 删除postgresql触发器语句
    public static final String DROP_TRIGGER_POSTGRESQL_TEMPLATE = "DROP TRIGGER %s ON %s; DROP FUNCTION %s()";

    // 查询语句
    public static final String QUERY_SQL_TEMPLATE = "SELECT %s FROM %s WHERE %s ORDER BY %s";

    public static final String QUERY_SQL_TEMPLATE_WITHOUT_ORDER = "SELECT %s FROM %s WHERE %s";

    public static final String QUERY_SQL_TEMPLATE_WITHOUT_WHERE = "SELECT %s FROM %s ORDER BY %s";

    public static final String QUERY_SQL_TEMPLATE_WITHOUT_WHERE_AND_ORDER = "SELECT %s FROM %s";

    // 插入语句
    public static final String INSERT_SQL_TEMPLATE = "INSERT INTO %s (%s) VALUES(%s)";

    // 更新语句
    public static final String UPDATE_SQL_TEMPLATE = "UPDATE %s SET %s WHERE %s";

    // 删除语句
    public static final String DELETE_SQL_TEMPLATE = "DELETE FROM %s WHERE %s";

    // truncate语句
    public static final String TRUNCATE_SQL_TEMPLATE = "TRUNCATE TABLE %s";

    public static final int ERROR_INT = -1;

    public static final String TRUE = "1";

    public static final String CATALOG = "catalog";
    public static final String SCHEMA = "schema";
    public static final String TABLE = "table";
    public static final String COLUMN = "column";
    public static final String PRIMARY = "primary";
}
