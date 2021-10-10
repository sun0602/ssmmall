package com.ssm.mall.dao;

import com.ssm.mall.dao.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectByParentId(@Param("parent_id") Integer parentId);

}