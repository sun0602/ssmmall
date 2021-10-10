package com.ssm.mall.action.portal;

import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.vo.ProductDetailVo;
import com.ssm.mall.service.iservice.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    ProductService productService;

    @ResponseBody
    @RequestMapping("detail.do") //前端获取上架的商品详情
    public ServerRes<ProductDetailVo> getDetail(Integer id) {
        //用户无需登录，就可以获得商品详情
        return productService.getProductDetail(id);
    }

    @ResponseBody
    @RequestMapping("list.do")
    //根据关键字（keywords）或CategoryId查询商品列表
    public ServerRes<PageInfo> listByKeyCategory(
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "orderBy", defaultValue = "") String orderBy
    ) {
        //用户无需登录，就可以进行商品查询（关键字查询、品类查询）,并实现动态排序
        return productService.listByKeyCategory(keywords, categoryId, pageNum, pageSize, orderBy);
    }

}
