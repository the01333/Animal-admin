package com.puxinxiaolin.adopt.service;

import com.puxinxiaolin.adopt.entity.dto.LoginDTO;
import com.puxinxiaolin.adopt.entity.dto.RegisterDTO;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.LoginVO;
import com.puxinxiaolin.adopt.entity.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录结果（包含token和用户信息）
     */
    LoginVO login(LoginDTO loginDTO);
    
    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 用户ID
     */
    Long register(RegisterDTO registerDTO);
    
    /**
     * 用户登出
     */
    void logout();
    
    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    UserVO getCurrentUser();
    
    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserById(Long userId);
    
    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param userVO 用户信息
     * @return 是否成功
     */
    boolean updateUserInfo(Long userId, UserVO userVO);
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 邮箱验证码登录（不存在则自动注册）
     */
    LoginVO loginByEmailCode(String email, String code, String purpose);

    /**
     * 手机验证码登录（不存在则自动注册）
     */
    LoginVO loginByPhoneCode(String phone, String code, String purpose);

    /**
     * 上传用户头像
     * @param userId 用户ID
     * @param file 头像文件
     * @return 头像URL
     */
    String uploadAvatar(Long userId, org.springframework.web.multipart.MultipartFile file);
}

