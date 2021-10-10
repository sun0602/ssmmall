package com.ssm.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.CartDao;
import com.ssm.mall.dao.ProductDao;
import com.ssm.mall.dao.pojo.Cart;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.vo.CartItemVo;
import com.ssm.mall.dao.vo.CartVo;
import com.ssm.mall.service.iservice.CartService;
import com.ssm.mall.util.DecimalUtil;
import com.ssm.mall.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService {
    @Autowired
    CartDao cartDao;
    @Autowired
    ProductDao productDao;

    //工具方法：根据用户id获取购物车信息（CartVo和CartItemVo的拼装）
    private CartVo getCartVoByUserid(Integer userId) {
        //1-首先根据userId取出购物车所有信息
        List<Cart> cartList = cartDao.selectByUserid(userId);
        //2-装配CartItemVo和CartVo
        CartVo cartVo = new CartVo();
        List<CartItemVo> cartItemVoList = Lists.newArrayList();
        if (cartList != null && cartList.size() > 0) {
            for (Cart cart : cartList) {
                CartItemVo cartItemVo = new CartItemVo();
                //装配购物车信息
                cartItemVo.setId(cart.getId());
                cartItemVo.setProductId(cart.getProductId());
                cartItemVo.setUserId(cart.getUserId());
                cartItemVo.setChecked(cart.getChecked());//是否被选中
                //装配产品信息
                Integer productId = cart.getProductId();
                Product product = productDao.selectByPrimaryKey(productId);
                if (product != null) {
                    cartItemVo.setProName(product.getName());
                    cartItemVo.setProSubtitle(product.getSubtitle());
                    cartItemVo.setProMainImage(product.getMainImage());
                    cartItemVo.setProStatus(product.getStatus());
                    cartItemVo.setProPrice(product.getPrice());
                    cartItemVo.setProStock(product.getStock());
                    //根据商品库存，装配购物车中该商品的购买数量（最大为库存量）
                    int stock = product.getStock();
                    int quantity = cart.getQuantity();
                    if (quantity <= stock) {//库存充足
                        cartItemVo.setLimitQuantity(Const.Cart.QUANTITY_SUCCESS);
                    } else {//库存不足
                        cartItemVo.setLimitQuantity(Const.Cart.QUANTITY_OUT_OF_STOCK);
                        //将购物车选项中购买数量更新为库存数量
                        cart.setQuantity(stock);
                        cartDao.updateByPrimaryKeySelective(cart);
                    }
                    cartItemVo.setQuantity(cart.getQuantity());//装配购买数量
                    //计算该商品的结算总价
                    BigDecimal cartItemTotalPrice = DecimalUtil.mul(
                            product.getPrice().doubleValue(), cartItemVo.getQuantity());
                    cartItemVo.setCartItemTotalPrice(cartItemTotalPrice);
                }
                cartItemVoList.add(cartItemVo);
            }
        }
        System.err.println("**" + cartItemVoList);
        //装配整个购物车
        cartVo.setCartItemList(cartItemVoList);
        //计算整个购物车的结账总金额,以及判断是否被全部选中
        Boolean allChecked = true;
        BigDecimal cartTotalPrice = new BigDecimal("0");
        for (CartItemVo civ : cartItemVoList) {
            if (civ.getChecked() == Const.Cart.CHECKED) {//如果商品被选中
                System.err.println("*" + civ);
                cartTotalPrice = DecimalUtil.add(cartTotalPrice.doubleValue(),
                        civ.getCartItemTotalPrice().doubleValue());
            } else {
                allChecked = false;
            }
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(allChecked);//设置是否被全选
        //装配FTP文件服务器地址
        cartVo.setImageHost(PropertyUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    //向购物车中添加商品，返回整个购物车信息
    @Override
    public ServerRes<CartVo> addProduct(Integer userId, Integer productId, Integer num) {
        //处理商品插入
        Cart item = cartDao.selectByUseridAndProductid(userId, productId);
        if (item == null) {//说明购物车中没有该商品，则新增购物车商品选项
            Cart newItem = new Cart(userId, productId, num, Const.Cart.CHECKED);
            cartDao.insert(newItem);
        } else {//说明购物车中已经存在该商品，将数量累加,然后更新
            Integer newNum = item.getQuantity() + num;
            item.setQuantity(newNum);
            cartDao.updateByPrimaryKeySelective(item);
        }
        //取出购物车里所有商品，返回
        CartVo cartVo = getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS, cartVo);
    }

    @Override
    public ServerRes<CartVo> updateProductNum(Integer userId, Integer productId, Integer num) {
        //更新商品数量
        Cart item = cartDao.selectByUseridAndProductid(userId, productId);
        if (item != null) {
            item.setQuantity(num);
            cartDao.updateByPrimaryKeySelective(item);
        }
        //取出购物车里所有商品，返回
        CartVo cartVo = getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS, cartVo);
    }

    @Override
    public ServerRes<CartVo> deleteProductBatch(Integer userId, String productIds) {
        //使用guava的Splitter对字符串进行分隔，转化成字符串集合，用于SQL拼接
        List<String> ids = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(ids)) {
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        //批量删除商品
        int result = cartDao.deleteProductBatch(userId, ids);
        //取出购物车里所有商品，返回
        CartVo cartVo = getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS, cartVo);
    }

    @Override
    public ServerRes<CartVo> list(Integer userId) {
        CartVo cartVo = getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS, cartVo);
    }

    //全部商品选中或取消选中
    public ServerRes<CartVo> updateAllCheck(Integer userId, Integer checked) {
        cartDao.updateAllCheck(userId, checked);
        CartVo cartVo = getCartVoByUserid(userId);
        return ServerRes.success(Result.RESULT_SUCCESS, cartVo);
    }

    //单个商品选中或取消选中
    @Override
    public ServerRes<CartVo> updateProductCheck(Integer userId, Integer productId, Integer checked) {
        cartDao.updateupdateProductCheck(userId, productId, checked);
        return this.list(userId);
    }

    //购物车中购买商品的总数量  例如，商品1购买10个，商品2购买20个，则总数为30
    @Override
    public ServerRes<Integer> productCount(Integer userId) {
        Integer count = cartDao.productCount(userId);
        return ServerRes.success(Result.RESULT_SUCCESS, count);
    }


}
