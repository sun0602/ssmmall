package com.ssm.mall.service.impl;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.UserDao;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.UserService;
import com.ssm.mall.util.GuavaCache;
import com.ssm.mall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public ServerRes<User> login(String username, String password) {
        //判断用户名是否存在
        int countFlag = userDao.checkUsername(username);
        if (countFlag < 1) {
            return ServerRes.error(Result.USER_NOT_EXISTS);
        }
        //判断密码是否匹配
        User user = userDao.login(username, MD5Util.MD5EncodeUtf8(password));
        if (user == null) {
            return ServerRes.error(Result.PASSWORD_ERROR);
        }
        //登录成功，返回用户信息(需要将用户密码清空)
        user.setPassword(StringUtils.EMPTY);
        return ServerRes.success(Result.LOGIN_SUCCESS, user);
    }

    @Override
    public ServerRes checkValid(String validName, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (type.equals(Const.ValidType.USERNAME)) {
                int uflag = userDao.checkUsername(validName);
                if (uflag > 0) {
                    return ServerRes.error(Result.USER_ALREADY_EXIST);
                }
            } else if (type.equals(Const.ValidType.EMAIL)) {
                int eflag = userDao.checkEmail(validName);
                if (eflag > 0) {
                    return ServerRes.error(Result.EMAIL_ALREADY_EXIST);
                }
            } else {
                return ServerRes.error(Result.REGISTRY_ILLEAGEL_ARGUMENT);
            }
        }
        return ServerRes.success(Result.REGISTRY_VALID_SUCCESS);
    }

    @Override
    public ServerRes registry(User user) {
        //判断用户名是否已存在
        ServerRes uRes = this.checkValid(user.getUsername(), Const.ValidType.USERNAME);
        if (uRes.getStatus() == Result.USER_ALREADY_EXIST.getStatus()) {
            return uRes; }
        //判断EMAIL是否已存在
        ServerRes eRes = this.checkValid(user.getEmail(), Const.ValidType.EMAIL);
        if (eRes.getStatus() == Result.EMAIL_ALREADY_EXIST.getStatus()) {
            return eRes; }
        //如果用户名和EMAIL都不存在，开始执行注册
        user.setRole(Const.Role.USER);//1-设置用户的默认权限是普通用户
        String originPassword = user.getPassword();
        user.setPassword(MD5Util.MD5EncodeUtf8(originPassword));//2-对用户密码进行MD5加密
        //3-注册
        int regCount = userDao.insert(user);//思考：此处是否可以使用动态insert方法？）
        return regCount > 0 ? ServerRes.success(Result.REGISTRY_SUCCESS) : ServerRes.error(Result.REGISTRY_ERROR);
    }

    @Override
    public ServerRes<String> getQuestionByUsername(String username) {
        //判断用户名是否存在
        int uFlag = userDao.checkUsername(username);
        if(uFlag < 1){
            return ServerRes.error(Result.USER_NOT_EXISTS);
        }else{
            //如果存在，取出预设问题
            String question = userDao.getQuestionByUsername(username);
            //根据预设问题是否为空，反馈不同信息
            if(StringUtils.isNotBlank(question)) {
                return ServerRes.success(Result.RESULT_SUCCESS, question);
            }else{
                return ServerRes.error(Result.NO_PASSWORD_RESET_QUESTION);
            }
        }
    }

    //根据用户名和取出的问题，去验证预设问题
    @Override
    public ServerRes<String> checkPreAnswer(String username, String question, String answer) {
        int paFlag = userDao.checkPreAnswer(username,question,answer);
        if(paFlag > 0){
            //如果预设答案一致，则生成token令牌，存入GUAVA缓存
            //UUID是通用唯一识别码(UniversallyUniqueIdentifier)的缩写，例如：9e3e3d65-77d0-4811-b0d3-77336bad8590
            String tokenValue = UUID.randomUUID().toString();
            GuavaCache.setTokenToCache(Const.TOKEN_PREFIX+username,tokenValue);
            return ServerRes.success(Result.RESULT_SUCCESS,tokenValue);
        }
        return ServerRes.error(Result.PASSWORD_RESET_ANSWER_ERROR);
    }

    @Override
    public ServerRes resetPassword(String username, String token, String newPassword) {
        //验证token令牌参数是否正常传递
        if(StringUtils.isBlank(token)){
            return ServerRes.error(Result.NEED_TOKEN);
        }
        //验证用户名是否存在
        if(userDao.checkUsername(username) < 1){
            return ServerRes.error(Result.USER_NOT_EXISTS);
        }
        //验证服务器GUAVA缓存中的TOKEN令牌是否已过期
        String serverToken = GuavaCache.getTokenFromCache(Const.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(serverToken)){
            ServerRes.error(Result.TOKEN_EXPIRE);
        }
        //验证guava缓存中的令牌与客户提供的令牌是否一致
        if(StringUtils.equals(token,serverToken)){
            //此处要先将密码进行MD5加密，然后更新原密码
            int resetFlag = userDao.resetPassword(username,MD5Util.MD5EncodeUtf8(newPassword));
            if(resetFlag > 0) {
                return ServerRes.success(Result.PASSWORD_RESET_SUCCESS);
            }
        }else{
            return ServerRes.error(Result.TOKEN_ERROR);
        }
        return ServerRes.error(Result.PASSWORD_RESET_ERROR);
    }

    @Override
    public ServerRes modifyPassword(Integer id, String originPassword, String newPassword) {
        String userPwd = userDao.getPasswordById(id);
        if(StringUtils.equals(userPwd,MD5Util.MD5EncodeUtf8(originPassword))){
            int modifyFlag = userDao.modifyPassword(id,MD5Util.MD5EncodeUtf8(newPassword));
            if(modifyFlag > 0){
                return ServerRes.success(Result.MODIFY_PASSWORD_SUCCESS);
            }else{
                return ServerRes.error(Result.MODIFY_PASSWORD_ERROR);
            }
        }else{
            return ServerRes.error(Result.ORIGIN_PASSWORD_ERROR);
        }
    }

    @Override
    public ServerRes<User> getLoginUserInfo(Integer userid) {
        User  user = userDao.selectByPrimaryKey(userid);
        if(user == null){
            return ServerRes.error(Result.USER_NOT_FOUND);
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerRes.success(Result.RESULT_SUCCESS,user);
    }

    @Override
    public ServerRes<User> modifyLoginUserInfo(User newUser) {
        //业务要求，修改的email不能是其他用户已注册的邮箱地址，所以需要校验邮箱地址
        //思考：为什么不能使用已有的checkEmail？
        // 如果邮箱并没有更改，该邮箱地址依然存在于数据库中，检测报错，需要排除对象本身的userid
        int eFlag = userDao.checkEmailByUserId(newUser.getId(),newUser.getEmail());
        if(eFlag > 0){
            return ServerRes.error(Result.EMAIL_ALREADY_EXIST);
        }
        //更新用户信息
        int modifyUserFlag = userDao.updateByPrimaryKey(newUser);
        if(modifyUserFlag < 1){
            return ServerRes.error(Result.MODIFY_USER_ERROR);
        }
        return ServerRes.success(Result.RESULT_SUCCESS,newUser);
    }


}
