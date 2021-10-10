package com.ssm.mall.service.iservice;

import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Shipping;

public interface ShippingService {
    ServerRes insert(Integer userId, Shipping shipping);
    //传入userId，预防横向越权
    ServerRes deleteByUseridShippingid(Integer userId, Integer shippingId);

    ServerRes updateByUseridShipping(Integer userId, Shipping shipping);

    ServerRes<Shipping> selectByUseridShippingid(Integer userId, Integer shippingId);

    ServerRes<PageInfo> selectByUserId(Integer userId, Integer pageNum, Integer pageSize);
}
