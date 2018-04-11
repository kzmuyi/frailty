package com.yangyongqiang.frailty.dal.mapper;

import com.yangyongqiang.frailty.dal.po.TestPo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@Repository
public interface TestPoMapper {
    List<TestPo> selectAll();
}
