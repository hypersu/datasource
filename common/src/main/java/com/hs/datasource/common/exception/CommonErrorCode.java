package com.hs.datasource.common.exception;

import com.hs.datasource.common.ErrorCode;

/**
 *
 */
public enum CommonErrorCode implements ErrorCode {
    JSON_PROCESSING_EXCEPTION("Common-00", "Json处理异常."),
    RUNTIME_EXCEPTION("Common-01", "运行时内部调用错误."),
    ;

    private final String code;

    private final String describe;

    private CommonErrorCode(String code, String describe) {
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
