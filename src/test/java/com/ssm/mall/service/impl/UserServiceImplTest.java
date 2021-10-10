package com.ssm.mall.service.impl;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class UserServiceImplTest {
    @Autowired
    UserService userService;

    @Test
    public void login() {
        ServerRes<User> sr1 = userService.login("scott","tiger");
        System.err.println("********************"+sr1);
        ServerRes<User> sr2 = userService.login("scott11","tiger");
        System.err.println("********************"+sr2);
        ServerRes<User> sr3 = userService.login("scott","tiger11");
        System.err.println("********************"+sr3);

    }

    @Test
    public void checkValid() {
        //测试用户名存在
        ServerRes sr11 = userService.checkValid("scott", Const.ValidType.USERNAME);
        System.err.println(sr11);
        //测试用户名不存在
        ServerRes sr12 = userService.checkValid("scott11", Const.ValidType.USERNAME);
        System.err.println(sr12);
        //测试邮箱存在
        ServerRes sr21 = userService.checkValid("scott@hotmail.com", Const.ValidType.EMAIL);
        System.err.println(sr21);
        //测试邮箱不存在
        ServerRes sr22 = userService.checkValid("scott11@hotmail.com", Const.ValidType.EMAIL);
        System.err.println(sr22);
        //测试验证类型错误
        ServerRes sr3 = userService.checkValid("***", "someType");
        System.err.println(sr3);
    }

    @Test
    public void registry() {
        User user = new User("qian","123","qian@mall.com",
                "12345678911","qu","an");;
        ServerRes sr =  userService.registry(user);
        System.err.println("*******************************"+sr);
    }

    @Test
    public void checkPreAnswer() {
        System.err.println(userService.checkPreAnswer("scott","qu","an"));
        System.err.println(userService.checkPreAnswer("scott","qu","errAn"));
    }
}