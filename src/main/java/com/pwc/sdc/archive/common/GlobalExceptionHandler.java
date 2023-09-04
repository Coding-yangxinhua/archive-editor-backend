package com.pwc.sdc.archive.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.enums.ResultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局统一的异常处理，简单的配置下，根据自己的业务要求详细配置
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 宽泛的异常情况
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> onException(Exception ex) {
        //打印日志
        log.error(ex.getMessage());
        //todo 日志入库等等操作
        //统一结果返回
        return ResponseEntity.error();
    }

    /**
     * 参数校验异常步骤
     */

    @ExceptionHandler(value= {MethodArgumentNotValidException.class , BindException.class, ValidationException.class})
    public ResponseEntity<Map<String, String>> onValidException(Exception e) throws JsonProcessingException {
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException)e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException)e).getBindingResult();
        }
        Map<String,String> errorMap = new HashMap<>(16);
        bindingResult.getFieldErrors().forEach((fieldError)->
                errorMap.put(fieldError.getField(),fieldError.getDefaultMessage())
        );
        return ResponseEntity.error(ResultStatus.CHECK_ERROR);
    }
}