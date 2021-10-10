package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUseridAndProductid(
            @Param("userId") Integer userId,
            @Param("productId") Integer productId);

    List<Cart> selectByUserid(@Param("userId") Integer userId);

    int deleteProductBatch(@Param("userId") Integer userId, @Param("productIds") List<String> productIds);

    int updateAllCheck(@Param("userId") Integer userId, @Param("checked") Integer checked);

    void updateupdateProductCheck(@Param("userId") Integer userId,
                                  @Param("productId") Integer productId,
                                  @Param("checked") Integer checked);

   Integer productCount(@Param("userId") Integer userId);

   List<Cart> selectCheckedCarts(@Param("userId") Integer userId);
}