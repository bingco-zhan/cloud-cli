package com.by.base.exce;

/**
 * @Author: bingco
 * @Version: v1.0
 * @Description:
 * @Created: 2018年12月31日 10:07
 * @Package: adm-workspace - com.by.crm.wage.exception
 * <p>
 * -------------------------------------------------------------------
 */
public class BaseMessageException extends RuntimeException {

    public BaseMessageException() {
        super();
    }

    public BaseMessageException(String message) {
        super(message);
    }

    public BaseMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseMessageException(Throwable cause) {
        super(cause);
    }

    public BaseMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
