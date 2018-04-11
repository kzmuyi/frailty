package com.yangyongqiang.frailty.web.controller;

import com.yangyongqiang.frailty.biz.service.TestService;
import com.yangyongqiang.frailty.common.FrailtyResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Author Y_YQ
 * @DATE 10/4/18
 * @DESC
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @Resource
    private TestService service;

    @ResponseBody
    @RequestMapping("/test")
    private FrailtyResponse getAll(){
        FrailtyResponse response = FrailtyResponse.successResponse();
        response.setData(service.selectAll());
        return response;
    }
}
