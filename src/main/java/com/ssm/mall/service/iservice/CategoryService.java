package com.ssm.mall.service.iservice;

import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 添加目录
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerRes addCategory(String categoryName, Integer parentId);

    /**
     * 更新目录
     * @param categoryId
     * @param categoryName
     * @return
     */
    ServerRes updateCategory(Integer categoryId, String categoryName);

    /**
     * 获得指定parentId的所有平级子目录,非递归
     * @param parentId
     * @return
     */
    ServerRes<List<Category>> childrenCategory(Integer parentId);

    /**
     * 查询当前节点及所有递归子节点（深度递归）
     * @param categoryId
     * @return
     */
    ServerRes<List<Category>> getDeepCategory(Integer categoryId);

}
