package com.ssm.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.CategoryDao;
import com.ssm.mall.dao.ProductDao;
import com.ssm.mall.dao.pojo.Category;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.vo.ProductDetailVo;
import com.ssm.mall.dao.vo.ProductListVo;
import com.ssm.mall.service.iservice.CategoryService;
import com.ssm.mall.service.iservice.ProductService;
import com.ssm.mall.util.DateTimeUtil;
import com.ssm.mall.util.FTPUtil;
import com.ssm.mall.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    ProductDao productDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;
    @Override
    public ServerRes saveOrUpdate(Product product) {
        //将商品的图片集合中的第一张图选出，作为商品主图
        String imgs = product.getSubImages();
        int index = imgs.indexOf(",");
        String mainImg = imgs.substring(0, index);//截取出第一张图片
        product.setMainImage(mainImg);
        //如果product已存在，则更新，否则，新增
        if (productDao.selectByPrimaryKey(product.getId()) != null) {
            int updateRows = productDao.updateByPrimaryKeySelective(product);
            return updateRows > 0 ? ServerRes.success(Result.UPDATE_PRODUCT_SUCCESS) :
                    ServerRes.error(Result.UPDATE_PRODUCT_ERROR);
        } else {
            int insertRows = productDao.insert(product);
            return insertRows > 0 ? ServerRes.success(Result.INSERT_PRODUCT_SUCCESS) :
                    ServerRes.error(Result.INSERT_PRODUCT_ERROR);
        }
    }
    //更新商品状态（上架或下架）
    @Override
    public ServerRes setProductStatus(Integer id, Integer status) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(status);
        int rows = productDao.updateByPrimaryKeySelective(product);
        return rows>0? ServerRes.success(Result.UPDATE_STATUS_SUCCESS):
                ServerRes.error(Result.UPDATE_STATUS_ERROR);
    }
    //后台获取商品详情
    @Override
    public ServerRes<ProductDetailVo> getManageDetail(Integer id) {
        Product product = productDao.selectByPrimaryKey(id);
        if(product == null){
            return ServerRes.error(Result.PRODUCT_NOT_FOUND);
        }
        ProductDetailVo vo = assembleProductDetailVo(product);
        return ServerRes.success(Result.PRODUCT_DETAIL_SUCCESS,vo);
    }

    @Override
    public ServerRes<PageInfo> getManageList(int pageNum, int pageSize) {
        //PageHelper采用AOP面向切面编程
        //1-为PageHelper设置启动
        PageHelper.startPage(pageNum,pageSize);
        //2-查询
        List<Product> productList = productDao.selectList();
        //3-使用PageInfo封装分页后的结果，查询sql将被自动添加上limit语句
        PageInfo pageResult = new PageInfo(productList);
        //4-组装VO
        List<ProductListVo> vos = Lists.newArrayList();
        for(Product product:productList){
            ProductListVo plv = assembleProductListVo(product);
            vos.add(plv);
        }
        //将vo集合放入分页对象,替换原来的对象集合
        pageResult.setList(vos);
        return ServerRes.success(Result.RESULT_SUCCESS,pageResult);
    }
    @Override
    public ServerRes<PageInfo> searchByIdName(Integer id, String name, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(name != null) {
            name = new StringBuilder().append("%").append(name).append("%").toString();//变为模糊查询参数
        }
        List<Product> productList = productDao.searchByIdName(id,name);
        PageInfo pageResult = new PageInfo(productList);//3-使用PageInfo封装分页后的结果，查询sql自动添加上limit语句
        List<ProductListVo> vos = Lists.newArrayList();//4-组装VO
        for(Product product:productList){
            ProductListVo plv = assembleProductListVo(product);
            vos.add(plv);
        }
        //将vo集合放入分页对象,替换原来的对象集合
        pageResult.setList(vos);
        return ServerRes.success(Result.RESULT_SUCCESS,pageResult);
    }

    @Override
    public String upload(MultipartFile multipartFile, String path) {
        //获取上传的文件名及文件后缀
        String fileName = multipartFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));//.jpeg
        String uploadFileName = UUID.randomUUID()+suffix;
        logger.debug("开始上传，上传文件：{}，上传路径：{}，上传后的文件名：{}"
                ,fileName,path, uploadFileName);
        //检查上传路径是否已经存在，如果没有，就创建
        File uploadPath = new File(path);
        if(!uploadPath.exists()){
            uploadPath.setWritable(true);//开放文件夹的可写权限
            uploadPath.mkdirs();
        }
        //封装上传文件对象，实现上传
        File targetFile = new File(uploadPath,uploadFileName);
        try {
            multipartFile.transferTo(targetFile);//将文件上传到临时目录中
            //将临时目录中的文件targetFile以io流方式上传到ftp服务器上
            FTPUtil.upload("",Lists.newArrayList(targetFile));
            targetFile.delete();//删除临时文件夹下的文件targetFile
        } catch (IOException e) {
            logger.error(Result.PRODUCT_UPLOAD_IMAGE_ERROR.getMsg(), e);
            return null;
        }
        return targetFile.getName();
    }

    @Override
    public ServerRes<ProductDetailVo> getProductDetail(Integer id) {
        Product product = productDao.selectByPrimaryKey(id);
        //前台获得商品详情，需校验商品是否已经下架
        if(product == null || product.getStatus() != Const.Product.ON_SALE){
            return ServerRes.error(Result.PRODUCT_NOT_FOUND);
        }
        ProductDetailVo vo = assembleProductDetailVo(product);
        return ServerRes.success(Result.PRODUCT_DETAIL_SUCCESS,vo);
    }
    @Override
    public ServerRes<PageInfo> listByKeyCategory(
            String keywords, Integer categoryId, int pageNum, int pageSize,String orderBy) {

        if(StringUtils.isBlank(keywords) && categoryId == null){
            //keywords或categoryid二选一或都有，否则报错
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        // 对品类条件进行判断和处理(递归取出所有品类级别)
        List<Integer> categoryIds = Lists.newArrayList();
        if(categoryId != null){
            if(categoryDao.selectByPrimaryKey(categoryId) == null){
                //根据品类id没有取出品类对象
                return ServerRes.error(Result.CATEGORY_NO_FOUND);
            }else{
                //如果Category品类传输来的是父级品类，例如手机，而手机品类下又分智能手机、普通手机等等
                //此时需要调用CategoryService中的递归显示品类列表的功能
                List<Category> categorys = categoryService.getDeepCategory(categoryId).getData();
                for(Category category:categorys){
                    categoryIds.add(category.getId());
                }
            }
        }
        //对关键字条件keyWords进行判断和处理
        if(StringUtils.isNotBlank(keywords)) {
            keywords = "%" + keywords + "%";
        }
        //如果查询条件正常,初始化分页公共模块
        PageHelper.startPage(pageNum,pageSize);
        //对动态排序条件orderBy进行判断和处理
        // 例如输入price_desc，则sql内就是price desc
        if(Const.Product.PRICE_ASC_DESC.contains(orderBy)){
            String[] orderBySQL = orderBy.split("_");
            PageHelper.orderBy(orderBySQL[0]+" "+orderBySQL[1]);
        }

        List<ProductListVo> vos = Lists.newArrayList();
        List<Product> products = productDao.selectByKeywordsCategoryIds(
                StringUtils.isBlank(keywords)?null:keywords,
                categoryIds.size()==0?null:categoryIds);//对参数进行null值匹配
        PageInfo pageInfo = new PageInfo(products);//实现分页
        //组装VO，替换分页数据
        for(Product product:products){
            ProductListVo vo = assembleProductListVo(product);
            vos.add(vo);
        }
        pageInfo.setList(vos);
        return ServerRes.success(Result.RESULT_SUCCESS,pageInfo);
    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo vo = new ProductListVo();
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setSubtitle(product.getSubtitle());
        vo.setPrice(product.getPrice());
        vo.setMainImage(product.getMainImage());
        vo.setStatus(product.getStatus());
        //ImageHost从属性文件中读取信息
        vo.setImageHost(PropertyUtil.getProperty("ftp.server.http.prefix",
                "http://img.happymmall.com/"));
        return vo;
    }
    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo vo = new ProductDetailVo();
        vo.setId(product.getId());
        vo.setName(product.getName());
        vo.setStatus(product.getStatus());
        vo.setSubtitle(product.getSubtitle());
        vo.setDetail(product.getDetail());
        vo.setPrice(product.getPrice());
        vo.setStock(product.getStock());
        vo.setCategoryId(product.getCategoryId());
        vo.setSubImages(product.getSubImages());
        vo.setMainImage(product.getMainImage());
        //vo的时间是String类型，需要从POJO的Date类型进行转换
        //ImageHost从属性文件中读取信息
        vo.setImageHost(PropertyUtil.getProperty("ftp.server.http.prefix",
                "http://img.happymmall.com/"));
        //获得上一级目录id
        Category category = categoryDao.selectByPrimaryKey(product.getCategoryId());
        vo.setParentCategoryId(category.getParentId());
        //对时间进行处理，从mybatis取出时是一个毫秒数，需要转换成规定格式的String，用于view层
        String pattern = Const.STANDARD_DATETIME_FORMAT;//yyyy-MM-dd HH:mm:ss
        vo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime(),pattern));
        vo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime(),pattern));
        return vo;
    }
}
