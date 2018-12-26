package com.xuanxuan.csu.service.impl;

import com.xuanxuan.csu.Tester;
import com.xuanxuan.csu.model.Admin;
import com.xuanxuan.csu.service.AdminService;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class AdminServiceImplTest extends Tester {


    @Resource
    private AdminService adminService;


    @Before
    public void setUp() {
        Admin admin = new Admin();
        admin.setUsername("root");
        admin.setPassword("chen981030");
        adminService.save(admin);
    }

    /**
     * 管理员登陆测试
     */
    @Test
    public void login() {

        Admin admin = new Admin();
        admin.setUsername("root");
        admin.setPassword("chen981030");
        String sessionId = adminService.login(admin);
        assertNotNull(sessionId);

        admin.setUsername("xx");
        admin.setPassword("xx");
        String sessionId2 = adminService.login(admin);
        assertNull(sessionId2);
    }
}