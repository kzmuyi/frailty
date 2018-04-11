package com.yangyongqiang.frailty.biz.service;

import com.yangyongqiang.frailty.biz.BizTestBase;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
public class TestServiceTest extends BizTestBase{
    @Resource
    private TestService service;

    @Test
    public void selectAllTest(){
        printResult(service.selectAll());
    }
}
