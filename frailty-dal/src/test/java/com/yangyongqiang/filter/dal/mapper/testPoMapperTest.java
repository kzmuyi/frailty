package com.yangyongqiang.filter.dal.mapper;

import com.yangyongqiang.filter.dal.DalTestBase;
import com.yangyongqiang.frailty.dal.mapper.TestPoMapper;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */

public class testPoMapperTest extends DalTestBase {
    @Resource
    private TestPoMapper mapper;

    @Test
    public void selectAllTest(){
        printResult(mapper.selectAll());
    }
}
