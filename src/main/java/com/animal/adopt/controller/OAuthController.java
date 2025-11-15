package com.animal.adopt.controller;

import com.animal.adopt.common.Result;
import com.animal.adopt.entity.vo.LoginVO;
import com.animal.adopt.service.impl.OAuthStateService;
import com.animal.adopt.service.impl.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {
    private final OAuthStateService stateService;
    private final SocialLoginService socialLoginService;

    @Value("${wechat.app-id:}")
    private String wechatAppId;
    @Value("${wechat.redirect-uri:}")
    private String wechatRedirectUri;

    @Value("${qq.app-id:}")
    private String qqAppId;
    @Value("${qq.redirect-uri:}")
    private String qqRedirectUri;

    @GetMapping("/wechat/authorize")
    public Result<java.util.Map<String, String>> wechatAuthorize() {
        String state = stateService.createState();
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wechatAppId + "&redirect_uri=" + java.net.URLEncoder.encode(wechatRedirectUri, java.nio.charset.StandardCharsets.UTF_8) + "&response_type=code&scope=snsapi_userinfo&state=" + state + "#wechat_redirect";
        return Result.success(java.util.Map.of("url", url, "state", state));
    }

    @GetMapping("/wechat/callback")
    public Result<LoginVO> wechatCallback(@RequestParam String code, @RequestParam String state) {
        if (!stateService.verifyAndConsume(state)) {
            return Result.error("state错误或过期");
        }
        LoginVO vo = socialLoginService.loginByWeChatCode(code);
        return Result.success(vo);
    }

    @GetMapping("/qq/authorize")
    public Result<java.util.Map<String, String>> qqAuthorize() {
        String state = stateService.createState();
        String url = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + qqAppId + "&redirect_uri=" + java.net.URLEncoder.encode(qqRedirectUri, java.nio.charset.StandardCharsets.UTF_8) + "&state=" + state;
        return Result.success(java.util.Map.of("url", url, "state", state));
    }

    @GetMapping("/qq/callback")
    public Result<LoginVO> qqCallback(@RequestParam String code, @RequestParam String state) {
        if (!stateService.verifyAndConsume(state)) {
            return Result.error("state错误或过期");
        }
        LoginVO vo = socialLoginService.loginByQQCode(code);
        return Result.success(vo);
    }
}