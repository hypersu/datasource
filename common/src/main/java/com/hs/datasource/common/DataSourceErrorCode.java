package com.hs.datasource.common;

public enum DataSourceErrorCode implements ErrorCode {
    RUNTIME_EXCEPTION("DataSource-00", "运行时异常"),
    CLASS_EXCEPTION("DataSource-01", "类异常"),
    IO_EXCEPTION("DataSource-02", "IO异常"),
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
