package com.haojing.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.haojing.common.ServiceResponse;
import com.haojing.dao.CategoryMapper;
import com.haojing.pojo.Category;
import com.haojing.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;
    public ServiceResponse addCategory(String categoryName, Integer parentId){
        if (parentId == null || StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0){
            return ServiceResponse.createBySuccess("添加品类成功");
        }
        return ServiceResponse.createByErrorMessage("添加品类失败");
    }

    public ServiceResponse updateCategoryName(Integer categoryId, String categoryName){
        if (categoryId == null && StringUtils.isBlank(categoryName)){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0){
            return ServiceResponse.createBySuccess("更新成功");
        }
        return ServiceResponse.createByErrorMessage("更新失败");
    }

    public ServiceResponse<List<Category>> getChildenParalieCategory(Integer categoryId){
       List<Category> categories = categoryMapper.selectByCategoryId(categoryId);
        if (CollectionUtils.isEmpty(categories)){
            logger.info("未找到当前分类");
        }
        return ServiceResponse.createBySuccess(categories);
    }
    public ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildren(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null){
            for (Category category:categorySet){
                categoryIdList.add(category.getId());
            }
        }
        return ServiceResponse.createBySuccess(categoryIdList);
    }
    private Set<Category> findChildren(Set<Category> categorySet, Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectByCategoryId(categoryId);
        for (Category category1:categoryList){
            findChildren(categorySet,category1.getId());
        }
        return categorySet;
    }
}
