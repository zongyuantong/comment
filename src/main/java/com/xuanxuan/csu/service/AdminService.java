package com.xuanxuan.csu.service;

import com.xuanxuan.csu.model.Admin;
import com.xuanxuan.csu.core.Service;


/**
 * Created by PualrDwade on 2018/12/22.
 */
public interface AdminService extends Service<Admin> {

    /**
     * 使用管理员账号进行登录
     *
     * @return
     */
    public String login(Admin admin);
}
