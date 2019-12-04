package com.haojing.dao;

import com.haojing.pojo.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> slectList();

    List<Product> selectProductByNameAndId(@Param("productName") String productName,@Param("productId") Integer productId);

    List<Product> selectByNameAndCategoryIds(@RequestParam(value = "productName") String productName,@RequestParam(value = "cat4egoryIdList") List<Integer> cat4egoryIdList);
}