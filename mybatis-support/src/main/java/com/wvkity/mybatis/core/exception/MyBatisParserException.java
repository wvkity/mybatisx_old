package com.wvkity.mybatis.core.exception;

/**
 * MyBatis解析异常
 * @author wvkity
 */
public class MyBatisParserException extends MyBatisException {

    private static final long serialVersionUID = -6444616123358626573L;

    public MyBatisParserException() {
        super();
    }

    public MyBatisParserException(String message) {
        super(message);
    }

    public MyBatisParserException(Throwable cause) {
        super(cause);
    }

    public MyBatisParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyBatisParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
