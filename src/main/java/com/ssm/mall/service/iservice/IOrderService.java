package com.ssm.mall.service.iservice;

import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.vo.OrderVO;
import com.ssm.mall.dao.vo.SearchVO;

public interface IOrderService {

    public ServerRes pay(Integer userId, Long orderNo, String path);
    public ServerRes createOrder(Integer userId, Integer shippingId);
    public ServerRes cancelOrder(Integer userId, Long orderNo);
    public ServerRes productsPreview(Integer userId);
    public ServerRes<OrderVO> getDetailByOrderNo(Integer userId, Long orderNo);
    public ServerRes getDetailByOrderNo(Long orderNo);
    public ServerRes<PageInfo> list(Integer userId, Integer page, Integer size);

    public ServerRes mlist(Integer page, Integer size);


    ServerRes msearch(SearchVO sv, Integer page, Integer size);

    ServerRes updateOrderStatusSend(Long orderNo);
}
