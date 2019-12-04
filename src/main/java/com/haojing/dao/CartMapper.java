package com.haojing.dao;

import com.haojing.pojo.Cart;
import javafx.scene.chart.ValueAxis;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param(value = "userId") Integer userId,@Param(value = "productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatus(Integer usetId);

    /**
     * @param userId
     * @param productIds
     * @return
     */
    int deleteByUserIdAndProductIds(@Param(value = "userId") Integer userId,@Param(value = "productIds") List<String> productIds);

    int checkedOrUncheckedAll(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId, @Param(value = "checked") Integer checked);

    int selectCartProductCount(Integer userId);
}