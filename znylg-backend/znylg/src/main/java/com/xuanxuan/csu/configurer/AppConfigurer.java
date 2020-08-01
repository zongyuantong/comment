package com.xuanxuan.csu.configurer;


/**
 * 系统业务基本配置类,主要进行参数的配置
 */
public final class AppConfigurer {
    public static final String DEFAULT_PASSAGE_URL = "www.znylgtest.com";
    public static final String DEFAULT_PLATFORM = "云麓谷评论";
    public static final int COMMENT_PAGE_SIZE = 10;//默认每次请求文章的评论获取10条数据
    public static final int COMMENT_REPLAY_NUMBER = 4;//文章主页每条评论下回复展示数量
    public static final int LOGIN_SESSION_TIME = 36000;//默认登陆的时间
    public static final String BASE_PACKAGE = "com.xuanxuan.csu";//生成代码所在的基础包名
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";//Mapper插件基础接口的完全限定名
    public static final String CONTROLLER_PACKAGE = BASE_PACKAGE + ".web";//生成的Controller所在包
    public static final String SERVICE_PACKAGE = BASE_PACKAGE + ".service";//生成的Service所在包
    public static final String SERVICE_IMPL_PACKAGE = SERVICE_PACKAGE + ".impl";//生成的ServiceImpl所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".dao";//生成的Mapper所在包
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".model";//生成的Model所在包

    public static final int VIRIFIED = 1;
    public static final int DOT_VIRIFIED = 0;

}