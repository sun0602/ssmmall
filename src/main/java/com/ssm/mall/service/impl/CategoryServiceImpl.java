package com.ssm.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.CategoryDao;
import com.ssm.mall.dao.pojo.Category;
import com.ssm.mall.service.iservice.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDao categoryDao;
    @Override
    public ServerRes addCategory(String categoryName,Integer parentId) {
        if(StringUtils.isBlank(categoryName) || parentId == null){
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        Category category = new Category(categoryName,parentId);
        category.setStatus(true);
        int addFlag = categoryDao.insert(category);
        if(addFlag > 0){
            return ServerRes.success(Result.CATEGORY_ADD_SUCCESS);
        }
        return ServerRes.error(Result.CATEGORY_ADD_ERROR);
    }

    @Override
    public ServerRes updateCategory(Integer categoryId, String categoryName) {
        if(StringUtils.isBlank(categoryName) || categoryId == null){
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        Category category = new Category(categoryId,categoryName);
        int addFlag = categoryDao.updateByPrimaryKeySelective(category);
        if(addFlag > 0){
            return ServerRes.success(Result.CATEGORY_UPDATE_SUCCESS);
        }
        return ServerRes.error(Result.CATEGORY_UPDATE_ERROR);
    }

    /**
     * 根据提供parentId获取下一级所有子节点（非递归）
     */
    @Override
    public ServerRes<List<Category>> childrenCategory(Integer parentId) {
        if(parentId == null){
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        List<Category> childrenCategory = categoryDao.selectByParentId(parentId);
        if(CollectionUtils.isEmpty(childrenCategory)){
            return ServerRes.error(Result.CATEGORY_NO_CHILDREN);
        }
        return ServerRes.success(Result.RESULT_SUCCESS,childrenCategory);
    }

    /**
     * 查询当前节点及所有递归子节点（深度递归）
     */
    @Override
    public ServerRes<List<Category>> getDeepCategory(Integer categoryId) {
        if(categoryId == null){
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        Set<Category> categorySet = Sets.newHashSet();//调用guava工具中的Sets，创建HashSet对象
        recursionDeepCategory(categoryId,categorySet);//递归查询的节点都将放入categorySet中
        //JSON转换时，List集合更为遍历，因此此处要将Set集合转换为List集合
        List<Category> categoryList = Lists.newArrayList();//调用guava工具中的Lists，创建ArrayList对象
        for(Category item:categorySet){
            categoryList.add(item);
        }
        return ServerRes.success(Result.RESULT_SUCCESS,categoryList);
    }

    /**
     * 递归节点及其所有子节点，返回Set集合
     */
    private Set<Category> recursionDeepCategory(Integer categoryId,Set<Category> categorySet){
        //1-在集合中添加当前节点
        Category categoryNow = categoryDao.selectByPrimaryKey(categoryId);
        if(categoryNow != null){
            categorySet.add(categoryNow);
        }
        //2-把当前节点作为父节点，查询出下一级所有平级子节点
        //注意，此处调用了MyBatis的方法，默认不会返回null集合，因此后边的遍历不用加入非空判断
        List<Category> childrenCategoryList = categoryDao.selectByParentId(categoryId);
        //3-递归查询，生成Set集合
        for(Category item:childrenCategoryList){
            recursionDeepCategory(item.getId(),categorySet);
        }
        return categorySet;
        /*思考执行流程：
         * 例如：下面的目录结构
         * 100（数码3C）
         *     -->  110(电脑)
         *          -->111(台式机)
         *          -->112(笔记本)
         *     --> 120（电器）
         * 执行流程：
         * 100加入集合后，会获得110和112
         * 遍历，递归加入110时，会获取111和112
         * 遍历，递归加入111，之后该分支的递归停止
         * 递归加入222，之后该分支的递归停止
         * 递归加入120，该分支的递归停止
         */
    }

}
