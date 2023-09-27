package com.pwc.sdc.archive.common.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.enums.ResultStatus;
import lombok.extern.slf4j.Slf4j;
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
    public Exception onException(Exception ex) throws Exception {
        //打印日志
        log.error(ex.getMessage());
        //todo 日志入库等等操作
        //统一结果返回
        throw ex;
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
        assert bindingResult != null;
        bindingResult.getFieldErrors().forEach((fieldError)->
                errorMap.put(fieldError.getField(),fieldError.getDefaultMessage())
        );
        return ResponseEntity.error(ResultStatus.CHECK_ERROR, errorMap.toString());
    }


    /**
     * 权限校验
     * @param e
     * @return
     * @throws JsonProcessingException
     */
    @ExceptionHandler(value= {NotRoleException.class , NotPermissionException.class, NotLoginException.class})
    public ResponseEntity<Map<String, String>> onAuthException(Exception e) throws JsonProcessingException {
        log.error("无权访问: {}", e.getMessage());
        return ResponseEntity.error(ResultStatus.AUTH_ERROR, e.getMessage());
    }

    /**
     * 参数异常
     * @param e
     * @return
     */
    @ExceptionHandler(value= {IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> onArgumentException(Exception e) {
        log.error("参数异常: {}", e.getMessage());
        return ResponseEntity.error(ResultStatus.BIZ_ERROR, e.getMessage());
    }
}