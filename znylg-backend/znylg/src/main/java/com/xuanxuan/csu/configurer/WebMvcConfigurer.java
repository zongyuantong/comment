package com.xuanxuan.csu.configurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xuanxuan.csu.configurer.interceptor.LogInterceptor;
import com.xuanxuan.csu.configurer.interceptor.SessionIdInterceptor;
import com.xuanxuan.csu.core.Result;
import com.xuanxuan.csu.core.ResultCode;
import com.xuanxuan.csu.core.ServiceException;
import com.xuanxuan.csu.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * @author PualrDwade
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    SessionIdInterceptor sessionIdInterceptor() {
        return new SessionIdInterceptor();
    }

    @Bean
    LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Resource
    RedisTemplate redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);

    @Value("${spring.profiles.active}")
    private String env;//当前激活的环境

    //使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        converters.add(converter);
    }


    //统一异常处理(rest api)
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add((request, response, handler, e) -> {
            Result result = new Result();
            if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                logger.info(e.getMessage());
            } else if (e instanceof MethodArgumentNotValidException) {//接口参数验证失败异常,如"id不能为空"
                String message = ((MethodArgumentNotValidException) e).getBindingResult().getFieldError().getDefaultMessage();
                result.setCode(ResultCode.FAIL).setMessage(message);
                logger.info(e.getMessage());
            } else if (e instanceof NoHandlerFoundException) {
                result.setCode(ResultCode.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
            } else if (e instanceof ServletException) {
                result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
            } else {
                result.setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员qq:544493924");
                String message;
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;
                    message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                            request.getRequestURI(),
                            handlerMethod.getBean().getClass().getName(),
                            handlerMethod.getMethod().getName(),
                            e.getMessage());
                } else {
                    message = e.getMessage();
                }
                logger.error(message, e);
            }
            CommonUtil.responseResult(response, result);
            return new ModelAndView();
        });
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("PUT", "DELETE", "GET", "POST", "PATCH")
                .allowedHeaders("*");
    }

    //统一添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (!"dev".equals(env)) { //开发环境忽略签名认证
            //1. 添加登陆验证拦截器
            registry.addInterceptor(sessionIdInterceptor());
        }
        //添加日志记录
        registry.addInterceptor(logInterceptor());

    }

    //添加swagger进行api测试与项目合作
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //生产环境不使用swagger
        if (!"prod".equals(env)) {
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
