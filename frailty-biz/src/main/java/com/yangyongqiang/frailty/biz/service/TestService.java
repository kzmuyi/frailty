package com.yangyongqiang.frailty.biz.service;

import com.yangyongqiang.frailty.dal.po.TestPo;

import java.util.List;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
public interface TestService {
    List<TestPo> selectAll();
}
