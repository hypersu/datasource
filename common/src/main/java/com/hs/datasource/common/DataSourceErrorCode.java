package com.hs.datasource.common;

public enum DataSourceErrorCode implements ErrorCode {
    CONNECTION_EXCEPTION("DataSource-01", "连接异常."),
    UNSUPPORTED_OPERATION("DataSource-02", "不支持的操作."),
    RUNTIME_EXCEPTION("DataSource-03", "出现运行时异常,请联系我们."),
    CLASS_NOT_FOUND_EXCEPTION("DataSource-04", "没找到相应的类型."),
    CLASS_EXCEPTION("DataSource-05", "类异常."),
    IO_EXCEPTION("DataSource-06", "IO异常."),
    ILLEGAL_VALUE("DataSource-07", "您填写的参数值不合法."),
    REQUIRED_VALUE("DataSource-08", "您缺失了必须填写的参数值."),
    ;

    private final String code;

    private final String describe;

    private DataSourceErrorCode(String code, String describe) {
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
