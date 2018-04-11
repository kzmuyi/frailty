package com.yangyongqiang.filter.dal;

import com.google.gson.Gson;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-ctx-dal.xml")
public class DalTestBase {
    private static final Gson GSON = new Gson();

    protected void printResult(Object obj) {
        System.out.println("---------\n"
                + Thread.currentThread().getStackTrace()[2].getMethodName()
                + ": " + GSON.toJson(obj)
                + "\n---------");
    }
}
