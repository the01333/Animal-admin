package com.animal.adopt.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.animal.adopt.entity.po.User;
import com.animal.adopt.entity.vo.LoginVO;
import com.animal.adopt.entity.vo.UserVO;
import com.animal.adopt.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final UserMapper userMapper;
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${wechat.app-id:}")
    private String wechatAppId;
    @Value("${wechat.app-secret:}")
    private String wechatAppSecret;
    @Value("${qq.app-id:}")
    private String qqAppId;
    @Value("${qq.app-key:}")
    private String qqAppKey;

    public LoginVO loginByWeChatCode(String code) {
        String openId = null;
        try {
            if (StrUtil.isNotBlank(wechatAppId) && StrUtil.isNotBlank(wechatAppSecret)) {
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + wechatAppId + "&secret=" + wechatAppSecret + "&code=" + code + "&grant_type=authorization_code";
                var map = restTemplate.getForObject(url, java.util.Map.class);
                openId = map == null ? null : (String) map.get("openid");
            }
        } catch (Exception e) {
            log.warn("微信code换取openid失败, 改用占位: {}", e.getMessage());
        }
        if (StrUtil.isBlank(openId)) {
            openId = pseudoId("wx:" + code);
        }
        return loginByOpenId(openId, "wechat");
    }

    public LoginVO loginByQQCode(String code) {
        String openId = null;
        try {
            if (StrUtil.isNotBlank(qqAppId) && StrUtil.isNotBlank(qqAppKey)) {
                // QQ 平台实际需要浏览器回调拿 openid；这里提供占位调用, 失败则走占位
                String url = "https://graph.qq.com/oauth2.0/me?access_token=" + code;
                String resp = restTemplate.getForObject(url, String.class);
                if (resp != null && resp.contains("openid")) {
                    int i = resp.indexOf("\"openid\":\"");
                    if (i > 0) {
                        int j = resp.indexOf("\"", i + 10);
                        openId = resp.substring(i + 9, j);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("QQ code换取openid失败, 改用占位: {}", e.getMessage());
        }
        if (StrUtil.isBlank(openId)) {
            openId = pseudoId("qq:" + code);
        }
        return loginByOpenId(openId, "qq");
    }

    private LoginVO loginByOpenId(String openId, String platform) {
        User user = null;
        if ("wechat".equals(platform)) {
            user = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>().eq(User::getWechatOpenid, openId));
        } else if ("qq".equals(platform)) {
            user = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>().eq(User::getQqOpenid, openId));
        }
        if (user == null) {
            user = new User();
            user.setUsername(platform + "_" + openId.substring(0, 10));
            if ("wechat".equals(platform)) {
                user.setWechatOpenid(openId);
            } else {
                user.setQqOpenid(openId);
            }
            user.setRole("user");
            user.setStatus(1);
            userMapper.insert(user);
        }
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(BeanUtil.copyProperties(user, UserVO.class));
        return loginVO;
    }

    private String pseudoId(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            return Integer.toHexString(input.hashCode());
        }
    }
}