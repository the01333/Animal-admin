package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.dto.LoginDTO;
import com.animal.adopt.dto.RegisterDTO;
import com.animal.adopt.service.UserService;
import com.animal.adopt.vo.LoginVO;
import com.animal.adopt.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Long> register(@Valid @RequestBody RegisterDTO registerDTO) {
        Long userId = userService.register(registerDTO);
        return Result.success("注册成功", userId);
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        userService.logout();
        return Result.success("登出成功", null);
    }
    
    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public Result<UserVO> getCurrentUser() {
        UserVO userVO = userService.getCurrentUser();
        return Result.success(userVO);
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<String> updateUserInfo(@Valid @RequestBody UserVO userVO) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.updateUserInfo(userId, userVO);
        return Result.success("更新成功", null);
    }
    
    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<String> changePassword(
            @NotBlank(message = "旧密码不能为空") @RequestParam String oldPassword,
            @NotBlank(message = "新密码不能为空") @RequestParam String newPassword) {
        Long userId = StpUtil.getLoginIdAsLong();
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success("密码修改成功", null);
    }
    
    /**
     * 根据ID查询用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }
}

