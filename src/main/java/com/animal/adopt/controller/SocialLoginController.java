package com.animal.adopt.controller;

import com.animal.adopt.common.Result;
import com.animal.adopt.entity.vo.LoginVO;
import com.animal.adopt.service.impl.SocialLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user/login")
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @PostMapping("/wechat")
    public Result<LoginVO> wechatLogin(@RequestBody Map<String, String> body) {
        String code = body.getOrDefault("code", "");
        LoginVO vo = socialLoginService.loginByWeChatCode(code);
        return Result.success("登录成功", vo);
    }

    @PostMapping("/qq")
    public Result<LoginVO> qqLogin(@RequestBody Map<String, String> body) {
        String code = body.getOrDefault("code", "");
        LoginVO vo = socialLoginService.loginByQQCode(code);
        return Result.success("登录成功", vo);
    }
}