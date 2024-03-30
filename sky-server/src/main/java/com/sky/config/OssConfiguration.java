package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建ALiOssUtil对象
 */
@Configuration   //表明该类是一个配置类，Spring 容器会加载这个类，并读取其中的配置信息。
@Slf4j
public class OssConfiguration {


    @Bean  //用于将方法返回的对象注册为 Spring 容器中的 Bean .在配置类中，可以通过在方法上添加 @Bean 注解来声明一个 Bean。Spring 容器会调用这个方法，将方法返回的对象注册为一个 Bean，并根据方法名作为 Bean 的名称。通过 @Bean 注解，可以实现自定义 Bean 的创建和配置，包括作用域、初始化方法、销毁方法等。
    @ConditionalOnMissingBean //当 Spring 容器中不存在指定类型的 Bean 时，@ConditionalOnMissingBean 注解标注的 Bean 才会被创建。
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象:{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                        aliOssProperties.getAccessKeyId(),
                        aliOssProperties.getAccessKeySecret(),
                        aliOssProperties.getBucketName());
    }
}
