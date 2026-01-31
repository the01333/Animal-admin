package com.puxinxiaolin.adopt.mapper;

import com.puxinxiaolin.adopt.entity.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 清理已删除用户的手机号（加上时间戳后缀避免唯一索引冲突）
     * @param phone 手机号
     */
    @Update("UPDATE t_user SET phone = CONCAT(phone, '_deleted_', UNIX_TIMESTAMP(NOW())), " +
            "username = CASE WHEN username = #{phone} THEN CONCAT(username, '_deleted_', UNIX_TIMESTAMP(NOW())) ELSE username END " +
            "WHERE deleted = 1 AND (phone = #{phone} OR username = #{phone})")
    void clearDeletedUserPhone(@Param("phone") String phone);
    
    /**
     * 清理已删除用户的邮箱（加上时间戳后缀避免唯一索引冲突）
     * @param email 邮箱
     */
    @Update("UPDATE t_user SET email = CONCAT(email, '_deleted_', UNIX_TIMESTAMP(NOW())), " +
            "username = CASE WHEN username = #{email} THEN CONCAT(username, '_deleted_', UNIX_TIMESTAMP(NOW())) ELSE username END " +
            "WHERE deleted = 1 AND (email = #{email} OR username = #{email})")
    void clearDeletedUserEmail(@Param("email") String email);
}

