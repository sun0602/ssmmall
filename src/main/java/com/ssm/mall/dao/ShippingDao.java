package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByUseridShippingid(
            @Param("userId") Integer userId,
            @Param("shippingId") Integer shippingId);

    int updateByUseridShippingid(Shipping shipping);

    Shipping selectByUseridShippingid(
            @Param("userId") Integer userId,
            @Param("shippingId") Integer shippingId);

    List<Shipping> selectByUserId(Integer userId);

}