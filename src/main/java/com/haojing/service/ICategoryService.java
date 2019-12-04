package com.haojing.service;

import com.haojing.common.ServiceResponse;
import com.haojing.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServiceResponse addCategory(String categoryName, Integer parentId);

    ServiceResponse updateCategoryName(Integer categoryId, String categoryName);

    ServiceResponse<List<Category>> getChildenParalieCategory(Integer categoryId);

    ServiceResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
