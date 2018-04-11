package com.yangyongqiang.frailty.biz.util;

import com.yangyongqiang.frailty.biz.BizTestBase;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
public class RedisClinetTest extends BizTestBase{
    @Resource
    private RedisClient client;

    @Test
    public void test(){
        printResult(client.get("a"));
    }
}
