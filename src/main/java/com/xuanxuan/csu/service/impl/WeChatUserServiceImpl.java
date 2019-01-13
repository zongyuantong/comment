package com.xuanxuan.csu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuanxuan.csu.configurer.AppConfigurer;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.dao.UserInfoMapper;
import com.xuanxuan.csu.dao.UserStarMapper;
import com.xuanxuan.csu.dto.UserDTO;
import com.xuanxuan.csu.model.UserInfo;
import com.xuanxuan.csu.model.UserStar;
import com.xuanxuan.csu.service.StarService;
import com.xuanxuan.csu.service.UserInfoService;
import com.xuanxuan.csu.core.AbstractService;
import com.xuanxuan.csu.vo.UserStateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by PualrDwade on 2018/12/03.
 */
@Service
@Transactional
public class WeChatUserServiceImpl extends AbstractService<UserInfo> implements UserInfoService {
    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private StarService starService;

    @Resource
    private RedisTemplate redisTemplate;

    //小程序的app_id
    private static final String appId = "wx94615bd0bc6e1d73";

    //小程序的secret_key
    private static final String secretKey = "ec948811317899de3436e55099716e42";

    //小程序的grant_type
    private static final String grantType = "authorization_code";

    /**
     * 后期加入单点登陆实现
     * 前端需要将sessonId维护在本地,并且在每个api请求head中加入
     *
     * @param userDTO
     * @return
     */
    @Override
    public Map<String, String> login(String code) {
        RestTemplate restTemplate = new RestTemplate();//使用resttemplete发送http请求
        String params = "appid=" + appId + "&secret=" + secretKey + "&js_code=" + code
                + "&grant_type=" + grantType;
        String url = "https://api.weixin.qq.com/sns/jscode2session?" + params;// 微信接口 用于查询oponid
        String response = restTemplate.getForObject(url, String.class);
        //转换成json对象
        JSONObject jsonObject = JSON.parseObject(response);
        //会话密钥(很重要,用来验证userInfo的真实性)
        String session_key = jsonObject.getString("session_key");
        //用户唯一标识
        String openId = jsonObject.getString("openid");
        //后续加入单点登陆验证
        if (openId == null || session_key == null) {
            throw new ServiceException("登陆api请求失败");
        }
        //通过openId和sessionId生成value作为sessionId的值
        String sessionId = UUID.randomUUID().toString();
        //存放到redis中,10个小时时间
        redisTemplate.opsForValue().set(sessionId, openId, AppConfigurer.LOGIN_SESSION_TIME, TimeUnit.SECONDS);
        //将会话id返回给前端,后续请求在header中加入sessionId,
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("openId", openId);
        return map;
    }

    @Override
    public void auth(UserDTO userDTO) {
        //对前端传来的用户信息进行校验
        if (validData(userDTO, "xxxxx")) {
            //判断用户是否已经存在,如果存在就更新,不存在则创建
            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userDTO.getId());
            if (userInfo == null) {
                //用户不存在,存入数据库
                userInfo = new UserInfo();
                BeanUtils.copyProperties(userDTO, userInfo);
                userInfoMapper.insert(userInfo);
            } else {
                //更新用户数据
                BeanUtils.copyProperties(userDTO, userInfo);
                userInfoMapper.updateByPrimaryKey(userInfo);
            }
        } else {
            throw new ServiceException("用户信息校验失败");
        }

    }

    @Override
    public UserStateVO getUserState(String openId) {

        UserStateVO userStateVO = new UserStateVO();

        //创建容器进行存储
        List<String> commentStarList = new ArrayList<>();
        List<String> replyStarList = new ArrayList<>();
        //1.得到用户的点赞
        Condition condition = new Condition(UserStar.class);
        condition.createCriteria().andCondition("user_id=", openId);
        List<UserStar> starList = starService.findByCondition(condition);
        starList.forEach(userStar -> {
            if (userStar.getToType().equals(1)) {
                commentStarList.add(userStar.getToId());
            } else {
                replyStarList.add(userStar.getToId());
            }
        });
        userStateVO.setCommentStarList(commentStarList);
        userStateVO.setReplyStarList(replyStarList);
        return userStateVO;
    }

    /**
     * 使用sessionKey校验数据
     *
     * @param userDTO
     * @param mdString
     * @return
     */
    private boolean validData(UserDTO userDTO, String mdString) {
        return true;
    }
}
