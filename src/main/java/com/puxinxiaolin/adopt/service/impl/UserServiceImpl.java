package com.puxinxiaolin.adopt.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.puxinxiaolin.adopt.enums.common.ResultCodeEnum;
import com.puxinxiaolin.adopt.entity.dto.*;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.LoginVO;
import com.puxinxiaolin.adopt.entity.vo.UserVO;
import com.puxinxiaolin.adopt.enums.UserRoleEnum;
import com.puxinxiaolin.adopt.exception.BizException;
import com.puxinxiaolin.adopt.mapper.UserMapper;
import com.puxinxiaolin.adopt.service.FileUploadService;
import com.puxinxiaolin.adopt.service.UserService;
import com.puxinxiaolin.adopt.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private VerificationCodeService verificationCodeService;
    
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginDTO loginDTO) {
        log.info("用户登录, 用户名: {}", loginDTO.getUsername());

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
        }

        // 验证密码
        if (!BCrypt.checkpw(loginDTO.getPassword(), user.getPassword())) {
            throw new BizException(ResultCodeEnum.PASSWORD_ERROR);
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BizException(ResultCodeEnum.USER_DISABLED);
        }

        // 登录
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        // 构造返回数据
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(buildUserVO(user));

        log.info("用户登录成功, 用户ID: {}", user.getId());
        return loginVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO loginByEmailCode(EmailCodeLoginDTO dto) {
        log.info("邮箱验证码登录: {}", dto.getEmail());
        if (!verificationCodeService.verifyEmailCode(dto.getEmail(), dto.getCode(), dto.getPurpose())) {
            throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "验证码错误或已过期");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, dto.getEmail());
        User user = this.getOne(wrapper);
        if (user != null && user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException(ResultCodeEnum.USER_DISABLED);
        }
        if (user == null) {
            user = new User();
            user.setUsername(dto.getEmail());
            user.setEmail(dto.getEmail());
            user.setRole("user");
            user.setStatus(1);
            this.save(user);
        }

        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(buildUserVO(user));
        return loginVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO loginByPhoneCode(PhoneCodeLoginDTO dto) {
        log.info("手机验证码登录: {}", dto.getPhone());
        if (!verificationCodeService.verifyPhoneCode(dto.getPhone(), dto.getCode(), dto.getPurpose())) {
            throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "验证码错误或已过期");
        }
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, dto.getPhone());
        User user = this.getOne(wrapper);
        if (user != null && user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException(ResultCodeEnum.USER_DISABLED);
        }
        if (user == null) {
            user = new User();
            user.setUsername(dto.getPhone());
            user.setPhone(dto.getPhone());
            user.setRole("user");
            user.setStatus(1);
            this.save(user);
        }
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();
        
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserInfo(buildUserVO(user));
        return loginVO;
    }

    /**
     * 注册
     *
     * @param registerDTO 注册信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(RegisterDTO registerDTO) {
        log.info("用户注册, 用户名: {}", registerDTO.getUsername());

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (this.count(wrapper) > 0) {
            throw new BizException(ResultCodeEnum.USER_ALREADY_EXISTS);
        }

        // 检查手机号是否已存在
        if (StrUtil.isNotBlank(registerDTO.getPhone())) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, registerDTO.getPhone());
            if (this.count(wrapper) > 0) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "手机号已被使用");
            }
        }

        // 检查邮箱是否已存在
        if (StrUtil.isNotBlank(registerDTO.getEmail())) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, registerDTO.getEmail());
            if (this.count(wrapper) > 0) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "邮箱已被使用");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(BCrypt.hashpw(registerDTO.getPassword()));
        user.setNickname("普通用户");
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setRole("user");
        user.setStatus(1);
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
            throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
        }

        return buildUserVO(user);
    }

    /**
     * 更新用户信息
     *
     * @param userVO 用户信息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCurrentUserInfo(UserVO userVO) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("更新用户信息, 用户ID: {}", userId);

        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
        }

        if (StrUtil.isNotBlank(userVO.getUsername())) {
            user.setUsername(userVO.getUsername());
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

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(ChangePasswordDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("修改密码, 用户ID: {}", userId);

        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
        }

        boolean hasExistingPassword = StrUtil.isNotBlank(user.getPassword());

        if (hasExistingPassword) {
            if (StrUtil.isBlank(dto.getOldPassword()) || !BCrypt.checkpw(dto.getOldPassword(), user.getPassword())) {
                throw new BizException(ResultCodeEnum.PASSWORD_ERROR);
            }
        }

        // 更新密码
        user.setPassword(BCrypt.hashpw(dto.getNewPassword()));
        return this.updateById(user);
    }

    /**
     * 根据ID查询用户信息
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public UserVO getUserById(Long userId) {
        log.info("根据ID查询用户信息, 用户ID: {}", userId);

        User user = this.getById(userId);
        if (user == null) {
            throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
        }

        return buildUserVO(user);
    }

    /**
     * 上传用户头像
     *
     * @param file 头像文件
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(MultipartFile file) {
        Long userId = StpUtil.getLoginIdAsLong();
        log.info("上传用户头像, 用户ID: {}", userId);

        if (file == null || file.isEmpty()) {
            throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "文件不能为空");
        }

        // 验证文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "只支持图片文件");
        }

        // 验证文件大小（限制为 5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "文件大小不能超过 5MB");
        }

        try {
            String avatarUrl = fileUploadService.uploadFile(file, "avatars");

            // 更新用户头像
            User user = this.getById(userId);
            if (user == null) {
                throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
            }

            user.setAvatar(avatarUrl);
            this.updateById(user);

            log.info("用户头像上传成功, 用户ID: {}, 头像URL: {}", userId, avatarUrl);
            return avatarUrl;
        } catch (Exception e) {
            log.error("上传用户头像失败, 用户ID: {}", userId, e);
            throw new BizException(ResultCodeEnum.INTERNAL_SERVER_ERROR.getCode(), "上传头像失败");
        }
    }

    /**
     * 分页获取用户列表
     *
     * @param current
     * @param size
     * @param keyword
     * @param role
     * @return
     */
    @Override
    public Page<UserVO> listPage(Long current, Long size, String keyword, String role) {
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

        Page<User> userPage = this.page(page, wrapper);
        Page<UserVO> result = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        result.setRecords(BeanUtil.copyToList(userPage.getRecords(), UserVO.class));
        return result;
    }

    /**
     * 验证 Token 有效性
     *
     * @return
     */
    @Override
    public Map<String, Object> verifyToken() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            User user = this.getById(userId);

            Map<String, Object> data = new HashMap<>();
            boolean enabled = user != null && (user.getStatus() == null || user.getStatus() == 1);
            data.put("valid", enabled);
            data.put("userId", userId);
            data.put("username", user != null ? user.getUsername() : null);
            data.put("role", user != null ? user.getRole() : null);

            return data;
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
//            Map<String, Object> data = new HashMap<>();
//            data.put("valid", false);
            throw new BizException(ResultCodeEnum.UNAUTHORIZED.getCode(), "Token已过期或无效");
        }
    }

    /**
     * 刷新 token
     *
     * @return
     */
    @Override
    public Map<String, Object> refreshToken() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            StpUtil.checkLogin();
            long tokenTimeout = StpUtil.getTokenTimeout();

            Map<String, Object> data = new HashMap<>();
            data.put("success", true);
            data.put("userId", userId);
            data.put("tokenTimeout", tokenTimeout);
            data.put("message", "Token已续约");

            log.debug("Token已续约, userId: {}, 剩余有效期: {}秒", userId, tokenTimeout);
            return data;
        } catch (Exception e) {
            log.warn("Token续约失败: {}", e.getMessage());
            throw new BizException(ResultCodeEnum.UNAUTHORIZED.getCode(), "Token续约失败");
        }
    }

    /**
     * 判断当前用户是否是管理员
     *
     * @return
     */
    @Override
    public boolean isAdmin() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = this.getById(userId);
        return user != null && StrUtil.isNotBlank(user.getRole()) &&
                ("super_admin".equals(user.getRole()) || "admin".equals(user.getRole()));
    }

    /**
     * 更新用户状态
     *
     * @param id
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long id, UpdateUserStatusDTO dto) {
        User user = this.getById(id);
        if (user == null) {
            throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
        }

        Integer oldStatus = user.getStatus();
        Integer newStatus = dto.getStatus();

        // 只有从「非禁用」变为「禁用」时才需要强制下线
        boolean needKick = (newStatus != null && newStatus == 0
                && (oldStatus == null || oldStatus != 0));

        user.setStatus(newStatus);
        this.updateById(user);

        if (needKick) {
            try {
                StpUtil.logout(user.getId());
                log.info("用户已被禁用并强制下线, userId={}", user.getId());
            } catch (Exception e) {
                log.warn("禁用用户时强制下线失败, userId={}, err={}", user.getId(), e.getMessage());
            }
        }
    }

    /**
     * 管理员更新用户信息
     *
     * @param id
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adminUpdateUser(Long id, AdminUpdateUserDTO dto) {
        User user = this.getById(id);
        if (user == null) {
            throw new BizException(ResultCodeEnum.USER_NOT_FOUND);
        }

        // 唯一性校验
        if (StrUtil.isNotBlank(dto.getUsername())) {
            boolean exists = this.count(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, dto.getUsername())
                    .ne(User::getId, id)) > 0;
            if (exists) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "用户名已被占用");
            }
            user.setUsername(dto.getUsername());
        }
        if (StrUtil.isNotBlank(dto.getPhone())) {
            boolean exists = this.count(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, dto.getPhone())
                    .ne(User::getId, id)) > 0;
            if (exists) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "手机号已被占用");
            }
            user.setPhone(dto.getPhone());
        }
        if (StrUtil.isNotBlank(dto.getEmail())) {
            boolean exists = this.count(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, dto.getEmail())
                    .ne(User::getId, id)) > 0;
            if (exists) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "邮箱已被占用");
            }
            user.setEmail(dto.getEmail());
        }

        Integer oldStatus = user.getStatus();
        boolean disabled = false;

        if (StrUtil.isNotBlank(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        if (StrUtil.isNotBlank(dto.getAvatar())) {
            user.setAvatar(dto.getAvatar());
        }
        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
            // 只有从非禁用 -> 禁用时才需要强制下线
            disabled = (dto.getStatus() == 0
                    && (oldStatus == null || oldStatus != 0));
        }
        if (StrUtil.isNotBlank(dto.getRole())) {
            UserRoleEnum roleEnum = UserRoleEnum.getByCode(dto.getRole());
            if (roleEnum == null) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "角色编码无效");
            }

            String currentRole = user.getRole();

            // 后端强校验：不允许通过接口创建新的超级管理员，也不允许修改现有超级管理员的角色
            boolean isCurrentSuperAdmin = UserRoleEnum.SUPER_ADMIN.getCode().equals(currentRole);
            boolean willBeSuperAdmin = (roleEnum == UserRoleEnum.SUPER_ADMIN);

            // 1) 如果当前是超级管理员，禁止改成任何其他角色
            if (isCurrentSuperAdmin && !willBeSuperAdmin) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "超级管理员角色不允许通过接口修改");
            }

            // 2) 如果当前不是超级管理员，禁止通过接口将其设为超级管理员
            if (!isCurrentSuperAdmin && willBeSuperAdmin) {
                throw new BizException(ResultCodeEnum.BAD_REQUEST.getCode(), "不允许通过接口创建或提升为超级管理员");
            }

            // 3) 其余情况（普通用户与管理员之间互相调整）允许修改
            user.setRole(roleEnum.getCode());
        }

        this.updateById(user);

        // 如果通过管理员编辑将用户禁用, 强制该用户所有会话下线
        if (disabled) {
            try {
                StpUtil.logout(user.getId());
                log.info("管理员编辑用户后将其禁用并强制下线, userId={}", user.getId());
            } catch (Exception e) {
                log.warn("管理员编辑用户禁用时强制下线失败, userId={}, err={}", user.getId(), e.getMessage());
            }
        }
    }

    /**
     * 构建用户详情 VO
     *
     * @param user
     * @return
     */
    private UserVO buildUserVO(User user) {
        if (user == null) {
            return null;
        }

        UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
        vo.setHasPassword(StrUtil.isNotBlank(user.getPassword()));
        return vo;
    }
}

