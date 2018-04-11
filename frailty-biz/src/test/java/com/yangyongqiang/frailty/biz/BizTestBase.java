package com.yangyongqiang.frailty.biz;

import com.google.gson.Gson;
import com.yangyongqiang.frailty.biz.service.BizTestConfigure;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BizTestConfigure.class)
public abstract class BizTestBase {
    private static final Gson GSON = new Gson();

    protected void printResult(Object obj) {
        System.out.println("---------\n"
                + Thread.currentThread().getStackTrace()[2].getMethodName()
                + ": " + GSON.toJson(obj)
                + "\n---------");
    }
}
