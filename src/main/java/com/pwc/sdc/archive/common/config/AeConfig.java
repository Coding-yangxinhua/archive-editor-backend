package com.pwc.sdc.archive.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.pwc.sdc.archive.mapper")
public class AeConfig {

}