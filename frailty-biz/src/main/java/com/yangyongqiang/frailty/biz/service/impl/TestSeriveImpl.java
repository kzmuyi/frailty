package com.yangyongqiang.frailty.biz.service.impl;

import com.yangyongqiang.frailty.biz.service.TestService;
import com.yangyongqiang.frailty.dal.mapper.TestPoMapper;
import com.yangyongqiang.frailty.dal.po.TestPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@Slf4j
@Component
public class TestSeriveImpl implements TestService{

    @Resource
    private TestPoMapper mapper;
    @Override
    public List<TestPo> selectAll() {
        return mapper.selectAll();
    }
}
