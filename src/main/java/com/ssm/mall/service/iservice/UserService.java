package com.ssm.mall.service.iservice;

import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;

public interface UserService {

    public ServerRes<User> login(String username,String password);

    /**
     * 检查用户名或邮箱是否已存在，不存在才能注册，会返回校验成功
     * @param validName  待检验名称
     * @param type  说明是用户名还是邮箱
     * @return
     */
    public ServerRes checkValid(String validName,String type);

    /**
     * 实现注册功能
     * @param user
     * @return
     */
    public ServerRes registry(User user);

    /**
     * 用户忘记密码，根据用户名获得密码重置的预设问题
     * @param username
     * @return
     */
    public ServerRes<String> getQuestionByUsername(String username);

    /**
     * 验证用户答案是否正确
     * @param answer
     * @return ServerRes<String>  将token放入ServerRes对象中
     */
    public ServerRes<String> checkPreAnswer(String username,String question,String answer);

    /**
     * 预设问题验证成功后，应用username和token对密码进行重置
     * @param username
     * @param token
     * @param newPassword
     * @return
     */
    public ServerRes resetPassword(String username,String token,String newPassword);

    /**
     * 已登录用户修改密码，需要先输入原密码进行校验
     * @param id
     * @param originPassword
     * @param newPassword
     * @return
     */
    ServerRes modifyPassword(Integer id, String originPassword, String newPassword);

    /**
     * 获得已登录用户的用户信息
     * @param userid
     * @return
     */
    ServerRes<User> getLoginUserInfo(Integer userid);

    /**
     * 修改已登录用户的用户信息
     * @param newUser
     * @return
     */
    ServerRes<User> modifyLoginUserInfo(User newUser);
}
