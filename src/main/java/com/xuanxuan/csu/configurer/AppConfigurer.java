package com.xuanxuan.csu.configurer;


/**
 * 系统业务基本配置类,主要进行参数的配置
 */
public final class AppConfigurer {
    public static final int COMMENT_PAGE_SIZE = 10;//默认每次请求文章的评论获取10条数据
    public static final int COMMENT_REPLAY_NUMBER = 4;//文章主页每条评论下回复展示数量
    public static final int LOGIN_SESSION_TIME = 36000;//默认登陆的时间
}