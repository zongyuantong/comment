package com.xuanxuan.csu.service;

import com.xuanxuan.csu.dto.UserDTO;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.core.Service;

import java.util.Map;


/**
 * Created by PualrDwade on 2018/12/03.
 */
public interface UserInfoService extends Service<UserInfo> {

    /**
     * 通过小程序的code请求验证身份,同时插入数据返回dto,提供给前端用户id信息
     *
     * @param code
     * @return
     */
    public Map<String, String> login(String code);


    /**
     * 进行用户授权,保存用户信息
     *
     * @param userDTO
     */
    public void auth(UserDTO userDTO);

}
