package com.xuanxuan.csu.vo;

import lombok.Data;

import java.util.List;

/**
 * 存储用户状态的VO(点赞..)
 */

@Data
public class UserStateVO {

    //用户的点赞评论
    private List<String> commentStarList;

    //用户的点赞回复
    private List<String> replyStarList;

}
