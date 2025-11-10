package com.animal.adopt.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.animal.adopt.common.Result;
import com.animal.adopt.entity.dto.LoginDTO;
import com.animal.adopt.entity.dto.RegisterDTO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.animal.adopt.common.ResultCode;
import com.animal.adopt.entity.po.User;
import com.animal.adopt.exception.BusinessException;
import com.animal.adopt.service.UserService;
import com.animal.adopt.entity.vo.LoginVO;
import com.animal.adopt.entity.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
     * 获取当前登录用户信息（兼容前端接口）
     */
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
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
    public Result<String> changePassword(@RequestBody java.util.Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        if (cn.hutool.core.util.StrUtil.isBlank(oldPassword) || cn.hutool.core.util.StrUtil.isBlank(newPassword)) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "旧密码和新密码不能为空");
        }
        Long userId = StpUtil.getLoginIdAsLong();
        userService.changePassword(userId, oldPassword, newPassword);
        return Result.success("密码修改成功", null);
    }
    
    /**
     * 获取用户列表（管理员）
     */
    @GetMapping("/list")
    public Result<Page<UserVO>> getUserList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                .or().like(User::getNickname, keyword)
                .or().like(User::getPhone, keyword)
                .or().like(User::getEmail, keyword));
        }
        if (StrUtil.isNotBlank(role)) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreateTime);
        
        Page<User> userPage = userService.page(page, wrapper);
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        voPage.setRecords(BeanUtil.copyToList(userPage.getRecords(), UserVO.class));
        
        return Result.success(voPage);
    }
    
    /**
     * 根据ID查询用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        UserVO userVO = userService.getUserById(id);
        return Result.success(userVO);
    }
    
    /**
     * 删除用户（管理员）
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功", null);
    }
    
    /**
     * 更新用户状态（管理员）
     */
    @PutMapping("/{id}/status")
    public Result<String> updateUserStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Integer> params) {
        Integer status = params.get("status");
        if (status == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "状态不能为空");
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        user.setStatus(status);
        userService.updateById(user);
        return Result.success("状态更新成功", null);
    }
}

