package com.puxinxiaolin.adopt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.puxinxiaolin.adopt.entity.dto.LoginDTO;
import com.puxinxiaolin.adopt.entity.dto.RegisterDTO;
import com.puxinxiaolin.adopt.entity.dto.EmailCodeLoginDTO;
import com.puxinxiaolin.adopt.entity.dto.PhoneCodeLoginDTO;
import com.puxinxiaolin.adopt.entity.dto.ChangePasswordDTO;
import com.puxinxiaolin.adopt.entity.dto.UpdateUserStatusDTO;
import com.puxinxiaolin.adopt.entity.entity.User;
import com.puxinxiaolin.adopt.entity.vo.LoginVO;
import com.puxinxiaolin.adopt.entity.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果（包含token和用户信息）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 用户注册
     *
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
     *
     * @return 用户信息
     */
    UserVO getCurrentUser();

    /**
     * 根据ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 更新用户信息
     *
     * @param userVO 用户信息
     * @return 是否成功
     */
    boolean updateCurrentUserInfo(UserVO userVO);

    /**
     * 修改密码
     *
     * @return 是否成功
     */
    boolean changePassword(ChangePasswordDTO changePasswordDTO);

    /**
     * 邮箱验证码登录（不存在则自动注册）
     */
    LoginVO loginByEmailCode(EmailCodeLoginDTO dto);

    /**
     * 手机验证码登录（不存在则自动注册）
     */
    LoginVO loginByPhoneCode(PhoneCodeLoginDTO dto);

    /**
     * 上传用户头像
     *
     * @param file   头像文件
     * @return 头像URL
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 分页获取用户列表
     *
     * @param current
     * @param size
     * @param keyword
     * @param role
     * @return
     */
    Page<UserVO> listPage(Long current, Long size, String keyword, String role);

    /**
     * 验证 Token 是否有效
     */
    Map<String, Object> verifyToken();

    /**
     * 续约 Token
     */
    Map<String, Object> refreshToken();

    /**
     * 是否管理员/审核员
     */
    boolean isAdmin();

    /**
     * 更新用户状态
     */
    void updateUserStatus(Long id, UpdateUserStatusDTO dto);
}
