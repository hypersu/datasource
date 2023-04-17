package com.hs.datasource.common.exception;

import com.hs.datasource.common.ErrorCode;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;

    public CommonException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public CommonException(ErrorCode errorCode, String errorMessage) {
        super(errorCode.toString() + " - " + errorMessage);
        this.errorCode = errorCode;
    }

    private CommonException(ErrorCode errorCode, String errorMessage, Throwable cause) {
        super(errorCode.toString() + " - " + getMessage(errorMessage) + " - " + getMessage(cause), cause);

        this.errorCode = errorCode;
    }

    public static CommonException asException(ErrorCode errorCode) {
        return new CommonException(errorCode);
    }

    public static CommonException asException(ErrorCode errorCode, String message) {
        return new CommonException(errorCode, message);
    }

    public static CommonException asException(ErrorCode errorCode, String message, Throwable cause) {
        if (cause instanceof CommonException) {
            return (CommonException) cause;
        }
        return new CommonException(errorCode, message, cause);
    }

    public static CommonException asException(ErrorCode errorCode, Throwable cause) {
        if (cause instanceof CommonException) {
            return (CommonException) cause;
        }
        return new CommonException(errorCode, getMessage(cause), cause);
    }

    private static String getMessage(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof Throwable) {
            return ((Throwable) obj).getMessage();
        } else {
            return obj.toString();
        }
    }

    public static String getTraceStack(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof Throwable) {
            StringWriter str = new StringWriter();
            PrintWriter pw = new PrintWriter(str);
            ((Throwable) obj).printStackTrace(pw);
            return str.toString();
        } else {
            return obj.toString();
        }
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

}