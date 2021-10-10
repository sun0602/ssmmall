package com.ssm.mall.service.iservice;

import com.github.pagehelper.PageInfo;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.vo.ProductDetailVo;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    public ServerRes saveOrUpdate(Product product);
    public ServerRes setProductStatus(Integer id, Integer status);
    public ServerRes<ProductDetailVo> getManageDetail(Integer id);
    public ServerRes<PageInfo> getManageList(int pageNum, int pageSize);
    public ServerRes<PageInfo> searchByIdName(Integer id, String name, int pageNum, int pageSize);
    //返回值为String，返回被成功上传的文件路径
    public String upload(MultipartFile multipartFile, String path);
    public ServerRes<ProductDetailVo> getProductDetail(Integer id);
    public ServerRes<PageInfo> listByKeyCategory(String keywords, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
