package com.pwc.sdc.archive.common.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MailService {
    @Autowired
    private AeUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String form;

    public boolean sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(form);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败{}", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean sendVerifyCode(String account, String key, String subject, String text) {
        // 生成验证码
        String code = RandomUtil.randomNumbers(4);
        // 判断发送验证码冷却时间
        Long coolDown = redisTemplate.getExpire(key + ":COOL-DOWN");

        if (coolDown != null && coolDown > 0) {
            return false;
        }
        // 存入缓存
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(key + ":COOL-DOWN", code, 60, TimeUnit.SECONDS);
        // 打上标签
        code = "<u>" + code + "</u>";
        // 替换text内容
        text = text.replace("${code}", code);
        // 发送验证码
        return this.sendMail(account,subject, text);
    }

    public boolean verifyCode(String key, String codeInput) {
        // 存入缓存
        String code = redisTemplate.opsForValue().get(key);
        // 验证码不存在或已过期
        if (!StringUtils.hasText(code)) {
            return false;
        }
        return ObjectUtil.equals(code, codeInput);
    }
}