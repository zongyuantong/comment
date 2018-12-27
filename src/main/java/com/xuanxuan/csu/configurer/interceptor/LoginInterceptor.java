package com.xuanxuan.csu.configurer.interceptor;


import com.alibaba.fastjson.JSON;
import com.xuanxuan.csu.configurer.announce.LoginRequired;
import com.xuanxuan.csu.configurer.AppConfigurer;
import com.xuanxuan.csu.configurer.WebMvcConfigurer;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 登陆状态校验的拦截器,对部分api进行登陆信息验证的拦截
 */

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    RedisTemplate redisTemplate;

    //日志记录
    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    /**
     * 重写拦截请求
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //首先判断是否是映射到方法的请求,不是方法直接通过

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //验证方法注解级别拦截器
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断此方法是否加上了登陆验证的注解
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        //如果有登陆验证注解,则验证身份
        if (methodAnnotation != null) {
            if (validLogin(request)) {
                return true;
            } else {
                logger.warn("sessionId验证失败，请求接口：{}，请求IP：{}，请求参数：{}",
                        request.getRequestURI(), getIpAddress(request), JSON.toJSONString(request.getParameterMap()));
                Result result = new Result();
                result.setCode(ResultCode.UNAUTHORIZED).setMessage("sessionId验证失败");
                responseResult(response, result);
                return false;
            }
        }
        //否则直接通过
        return true;
    }


    /**
     * 验证登陆
     *
     * @param request
     * @return
     */
    private boolean validLogin(HttpServletRequest request) {
        //从请求头中得到sessionId
        String sessionId = request.getHeader("sessionId");
        //请求头没有sessionId
        if (StringUtils.isEmpty(sessionId)) {
            return false;
        }
        //从缓存中取得sessonId的值
        String result = (String) redisTemplate.opsForValue().get(sessionId);
        //请求头的sessionId无效
        if (StringUtils.isEmpty(result)) {
            return false;
        }
        //存在此sessonId,说明验证成功,更新过期时间
        redisTemplate.opsForValue().set(sessionId, result, AppConfigurer.LOGIN_SESSION_TIME);
        return true;
    }


    /**
     * 得到请求的ip地址
     *
     * @param request
     * @return
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }


}
