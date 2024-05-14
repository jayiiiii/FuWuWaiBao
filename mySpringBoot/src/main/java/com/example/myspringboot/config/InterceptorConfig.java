package com.example.myspringboot.config;

import com.example.myspringboot.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration//配置类
public class InterceptorConfig  implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor())
////                .addPathPatterns("/**")//拦截所有请求，除去登陆注册、导入导出
////        .excludePathPatterns("/user/login","/user/register","/**/export","/**/import");
////              .addPathPatterns("/**")//拦截所有请求，除去登陆注册(测试先放行文件操作)
////                .excludePathPatterns("/staff/login","/staff/register","/**/export","/**/import","/file/**","/home");

}
    @Bean
    public JwtInterceptor jwtInterceptor(){
return new JwtInterceptor();//将对象注册到容器
    }
}
