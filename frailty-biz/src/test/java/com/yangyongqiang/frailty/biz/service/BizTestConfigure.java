package com.yangyongqiang.frailty.biz.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@Configuration
@ImportResource("classpath:test-ctx-biz.xml")
@PropertySource(value = {
        "classpath:memcached.properties",
        "classpath:redis.properties"}, ignoreResourceNotFound = true)
public class BizTestConfigure {
}

