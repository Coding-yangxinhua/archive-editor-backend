package com.pwc.sdc.archive.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Xinhua X Yang
 */
@Getter
public class CheckException extends RuntimeException {

    private static final long serialVersionUID = 2848146620399766500L;

    private Integer statusCode;

    private Object object;

    public CheckException(Integer statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }

    public CheckException(String msg) {
        super(msg);
        this.statusCode = HttpStatus.BAD_REQUEST.value();
    }

    public CheckException(String msg, Object object) {
        super(msg);
        this.statusCode = HttpStatus.BAD_REQUEST.value();
        this.object = object;
    }

}