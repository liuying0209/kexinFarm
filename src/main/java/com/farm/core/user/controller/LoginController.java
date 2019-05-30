package com.farm.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.farm.base.common.JsonResult;
import com.farm.core.user.LoginVO;
import com.farm.core.user.exception.UserException;
import com.farm.core.user.service.UserLoginService;
import com.farm.core.config.LoginCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 ** @Date: 2019-04-21 11:31
 */
@RestController
@RequestMapping("api/user")
public class LoginController {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserLoginService userLoginService;

    @PostMapping("/login")
    public JsonResult loginByPassword(LoginVO loginVO) throws UserException {
        LOGGER.info("请求开始报告 : 账号密码登入"+ JSONObject.toJSONString(loginVO));
        String token = this.userLoginService.loginByPassword(loginVO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);
        return JsonResult.ok(map);

    }

    @GetMapping("test")
    public String test(){

        return "ok";
    }

}
