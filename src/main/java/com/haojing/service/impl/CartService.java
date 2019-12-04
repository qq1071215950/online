package com.haojing.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.haojing.common.Const;
import com.haojing.common.ResponseCode;
import com.haojing.common.ServiceResponse;
import com.haojing.dao.CartMapper;
import com.haojing.dao.ProductMapper;
import com.haojing.pojo.Cart;
import com.haojing.pojo.Product;
import com.haojing.service.ICartService;
import com.haojing.utils.BigDecimalUtil;
import com.haojing.utils.PropertiesUtil;
import com.haojing.vo.CartProductVO;
import com.haojing.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.dc.pr.PRError;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService implements ICartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public ServiceResponse<CartVO> addCart(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if (cart == null){
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);
            cartMapper.insert(cartItem);
        }else {
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        CartVO cartVO = this.getCartVO(userId);
        return ServiceResponse.createBySuccess(cartVO);
    }

    public ServiceResponse<CartVO> updateCart(Integer userId, Integer productId, Integer count){
        if (productId == null || count == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if (cart != null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        CartVO cartVO = this.getCartVO(userId);
        return ServiceResponse.createBySuccess(cartVO);
    }

    public ServiceResponse<CartVO> deleteProduct(Integer userId, String productIds){
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId,productList);
        CartVO cartVO = this.getCartVO(userId);
        return ServiceResponse.createBySuccess(cartVO);
    }

    public ServiceResponse<CartVO> listCart(Integer userId){
        CartVO cartVO = this.getCartVO(userId);
        return ServiceResponse.createBySuccess(cartVO);
    }

    public ServiceResponse<CartVO> selectOrUnSelectAll(Integer userId,Integer productId,Integer checked){
        cartMapper.checkedOrUncheckedAll(userId,productId,checked);
        return this.listCart(userId);
    }

    public ServiceResponse<Integer> getCartProduct(Integer userId){
        if (userId == null){
            return ServiceResponse.createBySuccess(0);
        }
        return ServiceResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }
    private CartVO getCartVO(Integer userId){
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVO> cartProductVOList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if (! CollectionUtils.isEmpty(cartList)){
            for (Cart cartItem : cartList){
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cartItem.getId());
                cartProductVO.setUserId(userId);
                cartProductVO.setProductId(cartItem.getProductId());
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null){
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductSubTitle(product.getSubtitle());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStock(product.getStock());
                    // 库存
                    int buyLimitCount = 0;
                    if (product.getStock() >= cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        buyLimitCount = product.getStock();
                        cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVO.setQuantity(buyLimitCount);
                    // 计算总价
                    cartProductVO.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVO.getQuantity().doubleValue()));
                    cartProductVO.setProductChecked(cartItem.getChecked());
                }
                if (cartItem.getChecked() == Const.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setChecked(this.getCheckedAllStatus(userId));
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVO;
    }

    private boolean getCheckedAllStatus(Integer userId){
        if (userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatus(userId) == 0 ;
    }
}
