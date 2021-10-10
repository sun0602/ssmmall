package com.ssm.mall.action.portal;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UserAction {
    @Autowired
    UserService userService;

    //1.1登录
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public @ResponseBody
    ServerRes<User> login(
            @RequestParam String username,
            @RequestParam(value = "password", required = true) String password, HttpSession session) {
        ServerRes<User> result = userService.login(username, password);
        if (result.getStatus() == Result.LOGIN_SUCCESS.getStatus()) {
            session.setAttribute(Const.CURRENT_USER, result.getData());
        }
        return result;
    }

    //1.2注销
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    public @ResponseBody
    ServerRes logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerRes.success(Result.LOGOUT_SUCCESS);
    }

    //1.3用户注册
    @RequestMapping(value = "regist.do", method = RequestMethod.POST)
    public @ResponseBody
    ServerRes registUser(User user) {
        return userService.registry(user);
    }

    //1.4获取已登录的用户信息
    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.POST)
    public @ResponseBody
    ServerRes<User> getLoginedUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            //如果session中存在CURRENT_USER，表示用户已登录，返回session中的用户信息即可
            return ServerRes.success(Result.RESULT_SUCCESS, user);
        }
        return ServerRes.error(Result.NEED_LOGIN);
    }

    //1.5用户忘记密码，根据用户名获得密码重置的预设问题
    @RequestMapping(value = "getResetQuestion.do", method = RequestMethod.POST)
    public @ResponseBody ServerRes<String> getPasswordResetQuestion(String username) {
        return userService.getQuestionByUsername(username);
    }

    //1.6根据用户名、预设问题检验预设答案
    @RequestMapping(value = "checkPreAnswer.do", method = RequestMethod.POST)
    public @ResponseBody ServerRes<String> checkPreAnswer(String username,String question,String answer) {
        return userService.checkPreAnswer(username,question,answer);
    }

    //1.7根据Token令牌和用户名重置用户密码
    @RequestMapping(value = "resetPassword.do", method = RequestMethod.POST)
    public @ResponseBody ServerRes resetPassword(String username,String token,String newPassword) {
        return userService.resetPassword(username,token,newPassword);
    }

    //1.8登录状态下，对用户密码进行修改(需要输入原密码）
    @RequestMapping(value = "modifyPassword.do", method = RequestMethod.POST)
    public @ResponseBody ServerRes modifyPassword(
            String originPassword,//原密码
            String newPassword,//新密码
            HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if( user == null){
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return userService.modifyPassword(user.getId(),originPassword,newPassword);
    }
    @RequestMapping(value = "loginUserInfo.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes<User> getLoginUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return userService.getLoginUserInfo(user.getId());
    }
    @RequestMapping(value = "modifyUserInfo.do",method = RequestMethod.POST)
    public @ResponseBody ServerRes<User> modifyUserInfo(User newUser,HttpSession session){
        //从session中取出原user数据
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user ==  null){
            return ServerRes.error(Result.NEED_LOGIN);
        }
        //userId、username不允许更改，界面中不会出现文本框，因此要对newUser进行赋值
        newUser.setId(user.getId());
        newUser.setUsername(user.getUsername());
        ServerRes sr = userService.modifyLoginUserInfo(newUser);//更新用户信息
        if(sr.getStatus() == Result.RESULT_SUCCESS.getStatus()){// 更新成功，将更新后的用户信息放入session中
            session.setAttribute(Const.CURRENT_USER,newUser);
        }
        return sr; //返回成功信息
    }

}
