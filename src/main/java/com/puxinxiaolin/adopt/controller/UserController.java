package com.puxinxiaolin.adopt.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.common.Result;
import com.puxinxiaolin.adopt.entity.dto.*;
import com.puxinxiaolin.adopt.entity.vo.LoginVO;
import com.puxinxiaolin.adopt.entity.vo.UserVO;
import com.puxinxiaolin.adopt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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
    public Result<LoginVO> loginByEmailCode(@Valid @RequestBody EmailCodeLoginDTO dto) {
        LoginVO loginVO = userService.loginByEmailCode(dto);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 手机验证码登录（不存在则自动注册）
     */
    @PostMapping("/login/phone-code")
    public Result<LoginVO> loginByPhoneCode(@Valid @RequestBody PhoneCodeLoginDTO dto) {
        LoginVO loginVO = userService.loginByPhoneCode(dto);
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
        return Result.success(userService.verifyToken());
    }

    /**
     * Token续约接口
     * 用户在线时调用此接口, 自动续约Token的活跃时间
     * 类似Redisson的分布式锁续约机制
     * <p>
     * 工作原理：
     * 1. 用户每次操作时调用此接口
     * 2. 后端刷新Token的活跃时间
     * 3. 确保用户在线时Token永不过期
     * 4. 用户离线（未点击续约）时Token会过期
     */
    @PostMapping("/refresh-token")
    public Result<Map<String, Object>> refreshToken() {
        return Result.success(userService.refreshToken());
    }

    /**
     * 检查当前用户是否为管理员/审核员
     */
    @GetMapping("/is-admin")
    public Result<Boolean> isAdmin() {
        return Result.success(userService.isAdmin());
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public Result<String> updateUserInfo(@Valid @RequestBody UserVO userVO) {
        userService.updateCurrentUserInfo(userVO);
        return Result.success("更新成功", null);
    }

    /**
     * 上传用户头像
     */
    @PostMapping("/avatar/upload")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        Map<String, String> data = new HashMap<>();
        data.put("avatar", avatarUrl);
        return Result.success("头像上传成功", data);
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<String> changePassword(@Valid @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
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
        return Result.success(userService.listPage(current, size, keyword, role));
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
    public Result<String> updateUserStatus(@PathVariable Long id, @Valid @RequestBody UpdateUserStatusDTO dto) {
        userService.updateUserStatus(id, dto);
        return Result.success("状态更新成功", null);
    }

    /**
     * 管理员编辑用户信息/角色/状态
     */
    @PutMapping("/{id}/admin")
    @SaCheckRole(value = {"admin", "super_admin"}, mode = SaMode.OR)
    public Result<String> adminUpdateUser(@PathVariable Long id, @Valid @RequestBody AdminUpdateUserDTO dto) {
        userService.adminUpdateUser(id, dto);
        return Result.success("更新成功", null);
    }
}

