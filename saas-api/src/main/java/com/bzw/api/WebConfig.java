package com.bzw.api;

import com.bzw.common.handler.AccessHandler;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author yanbin
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    private AccessHandler accessHandler;

    @Autowired
    public WebConfig(AccessHandler accessHandler){
        this.accessHandler = accessHandler;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("X-Requested-With", "Content-Type","sessionId","Signature")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessHandler).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    public Gson gson(){
        return new Gson();
    }
}
