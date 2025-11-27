package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.entity.dto.LoginDTO;
import com.puxinxiaolin.adopt.entity.dto.RegisterDTO;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.LoginVO;
import com.puxinxiaolin.adopt.entity.vo.UserVO;
import com.puxinxiaolin.adopt.exception.BusinessException;
import com.puxinxiaolin.adopt.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
     * 邮箱验证码登录（不存在则自动注册）
     */
    @PostMapping("/login/email-code")
    public Result<LoginVO> loginByEmailCode(@RequestBody Map<String, String> params) {
        String email = params.get("email");
        String code = params.get("code");
        LoginVO loginVO = userService.loginByEmailCode(email, code, "login");
        return Result.success("登录成功", loginVO);
    }

    /**
     * 手机验证码登录（不存在则自动注册）
     */
    @PostMapping("/login/phone-code")
    public Result<LoginVO> loginByPhoneCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String code = params.get("code");
        LoginVO loginVO = userService.loginByPhoneCode(phone, code, "login");
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
     * 验证Token是否有效
     * 前端可以定期调用此接口检查Token是否过期
     */
    @GetMapping("/verify-token")
    public Result<Map<String, Object>> verifyToken() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            User user = userService.getById(userId);
            
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("valid", true);
            data.put("userId", userId);
            data.put("username", user != null ? user.getUsername() : null);
            data.put("role", user != null ? user.getRole() : null);
            
            return Result.success(data);
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("valid", false);
            return Result.error(ResultCode.UNAUTHORIZED.getCode(), "Token已过期或无效");
        }
    }

    /**
     * Token续约接口
     * 用户在线时调用此接口, 自动续约Token的活跃时间
     * 类似Redisson的分布式锁续约机制
     * 
     * 工作原理：
     * 1. 用户每次操作时调用此接口
     * 2. 后端刷新Token的活跃时间
     * 3. 确保用户在线时Token永不过期
     * 4. 用户离线（未点击续约）时Token会过期
     */
    @PostMapping("/refresh-token")
    public Result<Map<String, Object>> refreshToken() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            
            // Sa-Token会自动刷新Token的活跃时间
            // 这里我们显式调用一次, 确保活跃时间被刷新
            StpUtil.checkLogin();
            
            // 获取当前Token的剩余有效期
            long tokenTimeout = StpUtil.getTokenTimeout();
            
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("success", true);
            data.put("userId", userId);
            data.put("tokenTimeout", tokenTimeout);
            data.put("message", "Token已续约");
            
            log.debug("Token已续约, userId: {}, 剩余有效期: {}秒", userId, tokenTimeout);
            return Result.success(data);
        } catch (Exception e) {
            log.warn("Token续约失败: {}", e.getMessage());
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("success", false);
            return Result.error(ResultCode.UNAUTHORIZED.getCode(), "Token续约失败");
        }
    }

    /**
     * 检查当前用户是否为管理员/审核员
     */
    @GetMapping("/is-admin")
    public Result<Boolean> isAdmin() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        boolean admin = StrUtil.isNotBlank(user.getRole()) &&
                ("super_admin".equals(user.getRole()) || "admin".equals(user.getRole()) || "application_auditor".equals(user.getRole()));
        return Result.success(admin);
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
    public Result<String> changePassword(@RequestBody Map<String, String> params) {
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
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
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<Page<UserVO>> getUserList(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role
    ) {
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
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success("删除成功", null);
    }

    /**
     * 更新用户状态（管理员）
     */
    @PutMapping("/{id}/status")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> updateUserStatus(@PathVariable Long id, @RequestBody java.util.Map<String, Integer> params) {
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

