package com.xuanxuan.csu.service;

import com.xuanxuan.csu.dto.StarDTO;
import com.xuanxuan.csu.model.UserStar;
import com.xuanxuan.csu.core.Service;


/**
 * Created by PualrDwade on 2018/12/03.
 */
public interface StarService extends Service<UserStar> {
    /**
     * 点赞业务(考虑使用redis缓存,批量点赞)
     *
     * @param starDTO
     */
    public void zan(StarDTO starDTO);

}
