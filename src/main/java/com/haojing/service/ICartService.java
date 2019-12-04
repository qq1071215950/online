package com.haojing.service;

import com.haojing.common.ServiceResponse;
import com.haojing.vo.CartVO;

public interface ICartService {

    ServiceResponse<CartVO> addCart(Integer userId, Integer productId, Integer count);

    ServiceResponse<CartVO> updateCart(Integer userId, Integer productId, Integer count);

    ServiceResponse<CartVO> deleteProduct(Integer userId, String productIds);

    ServiceResponse<CartVO> listCart(Integer userId);

    ServiceResponse<CartVO> selectOrUnSelectAll(Integer userId,Integer productId,Integer checked);

    ServiceResponse<Integer> getCartProduct(Integer userId);
}
