package com.pwc.sdc.archive.service.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 基础的存档操作类：登录游戏，获得当前存档，上传存档等操作
 */
@Service
public class ArchiveHttpHandler {

    @Autowired
    private RestTemplate restTemplate;

    public String login(String url, String data) {
        RestTemplate restTemplate = new RestTemplate();
        //创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(data, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
       return responseEntity.getBody();
    }
    public void getCurrentArchive() {

    }

    public void uploadArchive() {}
}
