package com.ssm.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.ShippingDao;
import com.ssm.mall.dao.pojo.Shipping;
import com.ssm.mall.service.iservice.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    private ShippingDao shippingDao;

    @Override
    public ServerRes insert(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result = shippingDao.insert(shipping);
        if(result>0){
            Map resultMap = Maps.newHashMap();
            resultMap.put("shippingId",shipping.getId());
            return ServerRes.success(Result.RESULT_SUCCESS,resultMap);
        }
        return ServerRes.error(Result.RESULT_ERROR);
    }

    //避免横向越权  必须使用userId
    //否则用户登录后，可以尝试删除任意猜想到的shippingId（很有可能是别的用户的数据）
    @Override
    public ServerRes deleteByUseridShippingid(Integer userId, Integer shippingId) {
        int result = shippingDao.deleteByUseridShippingid(userId,shippingId);
        return result>0? ServerRes.success(Result.RESULT_SUCCESS): ServerRes.error(Result.RESULT_ERROR);
    }

    @Override
    public ServerRes updateByUseridShipping(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int result = shippingDao.updateByUseridShippingid(shipping);
        return result>0? ServerRes.success(Result.RESULT_SUCCESS)
                : ServerRes.error(Result.RESULT_ERROR);
    }

    @Override
    public ServerRes<Shipping> selectByUseridShippingid(Integer userId, Integer shippingId) {
        Shipping shipping = shippingDao.selectByUseridShippingid(userId,shippingId);
        return shipping==null? ServerRes.error(Result.RESULT_ERROR): ServerRes.success(Result.RESULT_SUCCESS,shipping);
    }

    @Override
    public ServerRes<PageInfo> selectByUserId(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingDao.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerRes.success(Result.RESULT_SUCCESS,pageInfo);
    }
}
