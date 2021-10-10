package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     *
     * @param username 用户名
     * @return 整数，计算有该用户名的用户数量
     */
    int checkUsername(String username);

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功，返回找到的用户信息，否则，返回NULL
     */
    User login(@Param("username") String username, @Param("password") String password);

    /**
     * 检查邮件是否已存在
     * @param email
     * @return
     */
    int checkEmail(String email);

    /**
     * 根据用户名查询密码重置的预设问题
     * @param username
     * @return
     */
    String getQuestionByUsername(String username);

    int checkPreAnswer(@Param("username") String username,
                       @Param("question") String question,
                       @Param("answer") String answer);

    int resetPassword(
            @Param("username") String username,
            @Param("newPassword") String newPassword);

    String getPasswordById(Integer id);

    int modifyPassword(
            @Param("userid") Integer id,
            @Param("newPassword") String newPassword);

    int checkEmailByUserId(
            @Param("userid") Integer userid,
            @Param("email") String email);

}