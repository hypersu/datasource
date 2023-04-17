package com.hs.datasource.rdbms;

import com.hs.datasource.common.ErrorCode;

public enum RdbmsErrorCode implements ErrorCode {
    RUNTIME_EXCEPTION("Rdbms-00", "运行时异常."),
    ILLEGAL_VALUE("Rdbms-01", "您填写的参数值不合法."),
    UNSUPPORTED_OPERATION_EXCEPTION("Rdbms-02", "您的操作错误."),
    UNSUPPORTED_DATABASE_TYPE("Rdbms-03", "您的数据库类型不被支持的."),
    INVALID_CONNECTION("Rdbms-04", "无效的数据库连接."),
    EMPTY_RESULT_EXCEPTION("Rdbms-05", "空数据异常."),
    INCORRECT_RESULT_SIZE_EXCEPTION("Rdbms-05", "不正确的数据条数异常."),
    STATEMENT_CALLBACK_EXCEPTION("Rdbms-06", "语句回调运行异常."),
    PREPARED_STATEMENT_CALLBACK_EXCEPTION("Rdbms-07", "预编译语句回调运行异常."),
    INVALID_SQL_STATEMENT("Rdbms-08", "不合法的sql语句."),
    ILLEGAL_TYPE("Rdbms-09", "不合法的类型."),
    METADATA_EXCEPTION("Rdbms-10", "元数据操作异常。"),
    UNSUPPORTED_META_TYPE("Rdbms-11", "不支持的元类型."),
    ;

    private final String code;

    private final String describe;

    private RdbmsErrorCode(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.describe;
    }

    @Override
    public String toString() {
        return String.format("Code:[%s], Describe:[%s]", this.code,
                this.describe);
    }
}
