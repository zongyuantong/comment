package com.xuanxuan.csu.configurer.interceptor;


import com.xuanxuan.csu.configurer.WebMvcConfigurer;
import com.xuanxuan.csu.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author PualrDwade
 * @create 2018-12-30
 * 日志拦截器
 */

@Component
public class LogInterceptor implements HandlerInterceptor {


    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    private CommonUtil commonUtil = new CommonUtil();

    /**
     * 实现拦截处理方法
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = commonUtil.getIpAddress(request);
        String url = request.getRequestURI();
        String params = request.getMethod();
        String querys = request.getQueryString();
        String method = request.getMethod();
        logger.info("请求的ip:" + ip + "请求url:" + url + "查询参数:" + querys + "方法:" + method);
        return true;
    }
}
