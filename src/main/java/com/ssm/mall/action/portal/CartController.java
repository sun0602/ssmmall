package com.ssm.mall.action.portal;

import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.dao.vo.CartVo;
import com.ssm.mall.service.iservice.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    CartService cartService;

    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<CartVo> addProduct(HttpSession session, Integer productId, Integer num) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        //参数校验
        if (productId == null || num == null) {
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        return cartService.addProduct(user.getId(),productId,num);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<CartVo> updateProductNum(HttpSession session, Integer productId, Integer num)
    {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        //参数校验
        if (productId == null || num == null) {
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        return cartService.updateProductNum(user.getId(),productId,num);
    }

    @RequestMapping(value = "delete.do", method = RequestMethod.POST)
    @ResponseBody
    //批量删除商品：productIds是用逗号分隔的id集合，例如"26,29,30"
    public ServerRes<CartVo> deleteProductBatch(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        //参数校验
        if (productIds == null) {
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        return cartService.deleteProductBatch(user.getId(),productIds);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<CartVo> listCart(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return cartService.list(user.getId());
    }
    @RequestMapping(value = "allChecked.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<CartVo> updateAllChecked(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return cartService.updateAllCheck(user.getId(), Const.Cart.CHECKED);
    }
    @RequestMapping(value = "allUnchecked.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<CartVo> updateAllUnchecked(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return cartService.updateAllCheck(user.getId(), Const.Cart.UNCHECKED);
    }

    @RequestMapping(value = "productChecked.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<CartVo> updateProductChecked(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return cartService.updateProductCheck(user.getId(),productId, Const.Cart.CHECKED);
    }
    @RequestMapping(value = "productUncheck.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<CartVo> updateProductUnchecked(HttpSession session, Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerRes.error(Result.NEED_LOGIN);
        }
        return cartService.updateProductCheck(user.getId(),productId, Const.Cart.UNCHECKED);
    }

    @RequestMapping(value = "productCount.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerRes<Integer> productCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null || user.getId() == null) {
            return ServerRes.success(Result.RESULT_SUCCESS,0);
        }
        return cartService.productCount(user.getId());
    }
}
