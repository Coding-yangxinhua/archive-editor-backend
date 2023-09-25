package com.pwc.sdc.archive.common.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.pwc.sdc.archive.common.bean.ResponseEntity;
import com.pwc.sdc.archive.common.constants.MailConstants;
import com.pwc.sdc.archive.domain.dto.AeUserDto;
import com.pwc.sdc.archive.service.AeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class MailService {
    @Autowired
    private AeUserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private JavaMailSender javaMailSender;


    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    public void sendVerifyCode(String key, String subject, String text) {
        // 获得用户邮箱信息
        long userId = StpUtil.getLoginIdAsLong();
        AeUserDto userDto = userService.getUserInfoById(userId);
        // 生成验证码
        String code = RandomUtil.randomNumbers(4);
        // 存入缓存
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        // 打上标签
        code = "<u>" + code + "</u>";
        // 替换text内容
        text = text.replace("${code}", code);
        // 发送验证码
        this.sendMail(userDto.getAccount(),subject, text);
    }

    public boolean verifyCode(String key, String codeInput) {
        // 存入缓存
        String code = redisTemplate.opsForValue().get(key);
        // 验证码不存在或已过期
        if (!StringUtils.hasText(codeInput)) {
            return false;
        }
        return ObjectUtil.equals(code, code);
    }
}