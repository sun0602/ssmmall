package com.ssm.mall.action.portal;

import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Shipping;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.service.iservice.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @RequestMapping("insert.do")
    @ResponseBody
    public ServerRes insert(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return shippingService.insert(user.getId(),shipping);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerRes delete(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return shippingService.deleteByUseridShippingid(user.getId(),shippingId);
    }
    @RequestMapping("update.do")
    @ResponseBody
    public ServerRes updateByUseridShippingid(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return  shippingService.updateByUseridShipping(user.getId(),shipping);
    }

    @RequestMapping("details.do")
    @ResponseBody
    public ServerRes<Shipping> detail(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
      return shippingService.selectByUseridShippingid(user.getId(),shippingId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerRes<PageInfo> list(
            HttpSession session,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return shippingService.selectByUserId(user.getId(),pageNum,pageSize);
    }



}
