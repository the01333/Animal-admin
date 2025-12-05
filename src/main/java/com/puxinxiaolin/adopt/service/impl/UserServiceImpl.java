package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.puxinxiaolin.adopt.common.ResultCode;
import com.puxinxiaolin.adopt.entity.dto.LoginDTO;
import com.puxinxiaolin.adopt.entity.dto.RegisterDTO;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.UserMapper;
import com.puxinxiaolin.adopt.service.UserService;
import com.puxinxiaolin.adopt.entity.vo.LoginVO;
import com.puxinxiaolin.adopt.entity.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import com.puxinxiaolin.adopt.service.VerificationCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final VerificationCodeService verificationCodeService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginDTO loginDTO) {
        log.info("用户登录, 用户名: {}", loginDTO.getUsername());
        
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        
        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new BizException(ResultCode.PASSWORD_ERROR);
        }
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BizException(ResultCode.USER_DISABLED);
        }
        
        // 登录
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        // 构造返回数据
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(BeanUtil.copyProperties(user, UserVO.class));
        
        log.info("用户登录成功, 用户ID: {}", user.getId());
        return loginVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO loginByEmailCode(String email, String code, String purpose) {
        log.info("邮箱验证码登录: {}", email);
        if (!verificationCodeService.verifyEmailCode(email, code, purpose)) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "验证码错误或已过期");
        }
        
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);
        User user = this.getOne(wrapper);
        if (user == null) {
            user = new User();
            user.setUsername(email);
            user.setEmail(email);
            user.setRole("user");
            user.setStatus(1);
            this.save(user);
        }
        
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(BeanUtil.copyProperties(user, UserVO.class));
        return loginVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO loginByPhoneCode(String phone, String code, String purpose) {
        log.info("手机验证码登录: {}", phone);
        if (!verificationCodeService.verifyPhoneCode(phone, code, purpose)) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "验证码错误或已过期");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = this.getOne(wrapper);
        if (user == null) {
            user = new User();
            user.setUsername(phone);
            user.setPhone(phone);
            user.setRole("user");
            user.setStatus(1);
            this.save(user);
        }
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(BeanUtil.copyProperties(user, UserVO.class));
        return loginVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(RegisterDTO registerDTO) {
        log.info("用户注册, 用户名: {}", registerDTO.getUsername());
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BizException(ResultCode.USER_ALREADY_EXISTS);
        }
        
        // 检查手机号是否已存在
        if (StrUtil.isNotBlank(registerDTO.getPhone())) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, registerDTO.getPhone());
            if (this.count(wrapper) > 0) {
                throw new BizException(ResultCode.BAD_REQUEST.getCode(), "手机号已被使用");
            }
        }
        
        // 检查邮箱是否已存在
        if (StrUtil.isNotBlank(registerDTO.getEmail())) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, registerDTO.getEmail());
            if (this.count(wrapper) > 0) {
                throw new BizException(ResultCode.BAD_REQUEST.getCode(), "邮箱已被使用");
            }
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword())); // 加密密码
        user.setNickname(registerDTO.getNickname());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setRole("user"); // 默认普通用户
        user.setStatus(1); // 正常状态
        user.setCertified(false);
        
        this.save(user);
        
        log.info("用户注册成功, 用户ID: {}", user.getId());
        return user.getId();
    }
    
    @Override
    public void logout() {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("用户登出, 用户ID: {}", userId);
        StpUtil.logout();
    }
    
    @Override
    public UserVO getCurrentUser() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        
        return BeanUtil.copyProperties(user, UserVO.class);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(Long userId, UserVO userVO) {
        log.info("更新用户信息, 用户ID: {}", userId);
        
        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        
        // 更新允许修改的字段
        if (StrUtil.isNotBlank(userVO.getNickname())) {
            user.setNickname(userVO.getNickname());
        }
        if (StrUtil.isNotBlank(userVO.getAvatar())) {
            user.setAvatar(userVO.getAvatar());
        }
        if (userVO.getGender() != null) {
            user.setGender(userVO.getGender());
        }
        if (userVO.getAge() != null) {
            user.setAge(userVO.getAge());
        }
        if (StrUtil.isNotBlank(userVO.getAddress())) {
            user.setAddress(userVO.getAddress());
        }
        if (StrUtil.isNotBlank(userVO.getOccupation())) {
            user.setOccupation(userVO.getOccupation());
        }
        if (StrUtil.isNotBlank(userVO.getHousing())) {
            user.setHousing(userVO.getHousing());
        }
        if (userVO.getHasExperience() != null) {
            user.setHasExperience(userVO.getHasExperience());
        }
        if (StrUtil.isNotBlank(userVO.getPhone())) {
            user.setPhone(userVO.getPhone());
        }
        if (StrUtil.isNotBlank(userVO.getEmail())) {
            user.setEmail(userVO.getEmail());
        }

        return this.updateById(user);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        log.info("修改密码, 用户ID: {}", userId);
        
        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        
        // 验证旧密码
        if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw new BizException(ResultCode.PASSWORD_ERROR);
        }
        
        // 更新密码
        user.setPassword(BCrypt.hashpw(newPassword));
        return this.updateById(user);
    }
    
    @Override
    public UserVO getUserById(Long userId) {
        log.info("根据ID查询用户信息, 用户ID: {}", userId);
        
        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }
        
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(Long userId, org.springframework.web.multipart.MultipartFile file) {
        log.info("上传用户头像, 用户ID: {}", userId);
        
        if (file == null || file.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "文件不能为空");
        }
        
        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "只支持图片文件");
        }
        
        // 验证文件大小（限制为 5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BizException(ResultCode.BAD_REQUEST.getCode(), "文件大小不能超过 5MB");
        }
        
        try {
            // 调用文件上传服务（这里假设有一个文件上传工具类）
            // 实际实现需要根据你的文件存储方案调整
            String avatarUrl = uploadFileToStorage(file);
            
            // 更新用户头像
            User user = this.getById(userId);
            if (user == null) {
                throw new BizException(ResultCode.USER_NOT_FOUND);
            }
            
            user.setAvatar(avatarUrl);
            this.updateById(user);
            
            log.info("用户头像上传成功, 用户ID: {}, 头像URL: {}", userId, avatarUrl);
            return avatarUrl;
        } catch (Exception e) {
            log.error("上传用户头像失败, 用户ID: {}", userId, e);
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "上传头像失败");
        }
    }
    
    /**
     * 上传文件到存储服务
     * 这是一个占位符方法, 实际实现需要根据你的文件存储方案调整
     * 可以使用 MinIO、阿里云 OSS、腾讯云 COS 等
     */
    private String uploadFileToStorage(org.springframework.web.multipart.MultipartFile file) throws Exception {
        // TODO: 实现文件上传逻辑
        // 这里返回一个示例 URL, 实际应该调用你的文件存储服务
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        return "/animal-adopt/avatar/" + filename;
    }
}

