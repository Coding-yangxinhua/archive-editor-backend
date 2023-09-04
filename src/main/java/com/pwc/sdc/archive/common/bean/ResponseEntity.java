package com.pwc.sdc.archive.common.bean;


import java.io.Serializable;

import com.pwc.sdc.archive.common.enums.ResultStatus;
import org.springframework.http.HttpStatus;

public class ResponseEntity<R> implements Serializable {
    private static final long serialVersionUID = -2774548248088883523L;
    private R result;
    private String message;
    private ResultStatus resultStatus;
    private int code;

    public ResponseEntity() {
        this.message = ResultStatus.OK.message();
        this.resultStatus = ResultStatus.OK;
        this.code = ResultStatus.OK.value();
    }

    public ResponseEntity(ResultStatus resultStatus) {
        this.message = ResultStatus.OK.message();
        this.resultStatus = ResultStatus.OK;
        this.code = ResultStatus.OK.value();
        this.resultStatus = resultStatus;
        this.code = resultStatus.value();
        this.message = resultStatus.message();
    }

    public ResponseEntity(ResultStatus resultStatus, R result) {
        this.message = ResultStatus.OK.message();
        this.resultStatus = ResultStatus.OK;
        this.code = ResultStatus.OK.value();
        this.result = result;
        this.resultStatus = resultStatus;
        this.code = resultStatus.value();
        this.message = resultStatus.message();
    }

    public ResponseEntity(ResultStatus resultStatus, String message, R result) {
        this.message = ResultStatus.OK.message();
        this.resultStatus = ResultStatus.OK;
        this.code = ResultStatus.OK.value();
        this.result = result;
        this.message = message;
        this.resultStatus = resultStatus;
        this.code = resultStatus.value();
    }

    public ResponseEntity(int code, String message, R result) {
        this.message = ResultStatus.OK.message();
        this.resultStatus = ResultStatus.OK;
        this.code = ResultStatus.OK.value();
        this.result = result;
        this.message = message;
        this.code = code;
    }

    public R getResult() {
        return this.result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultStatus getStatus() {
        return this.resultStatus;
    }

    public void setStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static <R> ResponseEntity<R> ok() {
        return (new Builder()).build();
    }

    public static <R> ResponseEntity<R> ok(R result) {
        return (new Builder(result)).build();
    }

    public static <R> ResponseEntity<R> error() {
        return error(ResultStatus.SYS_ERROR);
    }
    public static <R> ResponseEntity<R> error(ResultStatus resultStatus) {
        return new ResponseEntity(resultStatus, resultStatus.message());
    }

    public static <R> ResponseEntity<R> error(ResultStatus resultStatus, String message) {
        return new ResponseEntity(resultStatus, message, null);
    }

    public static <R> ResponseEntity<R> error(R result) {
        return new ResponseEntity(ResultStatus.SYS_ERROR, ResultStatus.SYS_ERROR.message(), result);
    }

    public static <R> Builder<R> builder() {
        return new Builder();
    }

    public static class Builder<R> {
        private R result;
        private ResultStatus resultStatus;
        private String message;
        private int code;

        public Builder(R result) {
            this.resultStatus = ResultStatus.OK;
            this.code = HttpStatus.OK.value();
            this.result = result;
        }

        public Builder() {
            this.resultStatus = ResultStatus.OK;
            this.code = HttpStatus.OK.value();
        }

        public <R> ResponseEntity<R> build() {
            return new ResponseEntity(this.code, this.message, this.result);
        }

        public Builder<R> status(ResultStatus resultStatus) {
            this.resultStatus = resultStatus;
            this.code = resultStatus.value();
            this.message = resultStatus.message();
            return this;
        }

        public Builder<R> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<R> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<R> result(R result) {
            this.result = result;
            return this;
        }
    }
}
