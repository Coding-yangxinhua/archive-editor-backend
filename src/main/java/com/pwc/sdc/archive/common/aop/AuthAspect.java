package com.pwc.sdc.archive.common.aop;

import cn.dev33.satoken.stp.StpUtil;
import com.pwc.sdc.archive.common.annotation.Auth;
import com.pwc.sdc.archive.common.constants.RoleConstants;
import io.netty.util.internal.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class AuthAspect {

    @Before("@annotation(com.pwc.sdc.archive.common.annotation.Auth)")
    public void beforeAdvice(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 获取注解
        Auth annotation = method.getAnnotation(Auth.class);
        StpUtil.checkLogin();
        // 获得所需角色和权限
        String[] roles = annotation.roles();
        String[] permissions = annotation.permissions();
        if (StpUtil.hasRole(RoleConstants.ADMIN)) {
            return;
        }
        if (roles != null && roles.length > 0) {
            StpUtil.checkRoleOr(roles);
        }
        if (permissions != null && permissions.length > 0) {
            StpUtil.checkPermissionOr(permissions);
        }
    }


}
