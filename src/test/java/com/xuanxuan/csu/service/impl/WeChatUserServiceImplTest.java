package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.dto.UserDTO;
import com.xuanxuan.csu.service.UserInfoService;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class WeChatUserServiceImplTest extends Tester {
    @Resource
    RedisTemplate redisTemplate;


    @Resource
    UserInfoService userInfoService;

    /**
     * 测试redis操作
     */
    @Test
    public void testRedis() {
        assertNotNull(redisTemplate);
        redisTemplate.opsForValue().set("chenzhixuan", "陈志轩");
        Object result = redisTemplate.opsForValue().get("chenzhixuan");
        System.out.println(result);
    }

    @Test
    public void testAuth() {
    }

    @Test
    public void testLogin() {

    }
}