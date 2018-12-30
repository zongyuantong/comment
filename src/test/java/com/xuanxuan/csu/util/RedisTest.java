package com.xuanxuan.csu.util;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.configurer.AppConfigurer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 测试redis连接
 */
public class RedisTest extends Tester {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void test1() {
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(sessionId, "test", AppConfigurer.LOGIN_SESSION_TIME, TimeUnit.SECONDS);
        String result = redisTemplate.opsForValue().get(sessionId);
        System.out.println(result);
        Assert.assertNotNull(result);
        Assert.assertEquals(result, "test");
    }
}
