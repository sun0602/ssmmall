package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKeyWithBLOBs(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectList();

    List<Product> searchByIdName(@Param("id") Integer id, @Param("name") String name);

    List<Product> selectByKeywordsCategoryIds(
            @Param("keywords") String keywords, @Param("categoryIds") List<Integer> categoryIds);
}