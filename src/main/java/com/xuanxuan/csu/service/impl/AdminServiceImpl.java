package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.configurer.AppConfigurer;
import com.xuanxuan.csu.dao.AdminMapper;
import com.xuanxuan.csu.model.Admin;
import com.xuanxuan.csu.service.AdminService;
import com.xuanxuan.csu.core.AbstractService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * Created by PualrDwade on 2018/12/22.
 */
@Service
@Transactional
public class AdminServiceImpl extends AbstractService<Admin> implements AdminService {
    @Resource
    private AdminMapper adminMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public String login(Admin admin) {
        Admin admin1 = adminMapper.selectByPrimaryKey(admin.getUsername());
        if (admin1 != null) {
            String sessionId = UUID.randomUUID().toString();
            //使用用户名+密码作为value存到redis中
            String token = admin.getUsername() + "," + admin.getPassword();
            redisTemplate.opsForValue().set(sessionId, token, AppConfigurer.LOGIN_SESSION_TIME);
            return sessionId;
        }
        return null;
    }
}
