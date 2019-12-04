package com.haojing.controller.portal;

import com.haojing.common.Const;
import com.haojing.common.ResponseCode;
import com.haojing.common.ServiceResponse;
import com.haojing.pojo.User;
import com.haojing.service.ICartService;
import com.haojing.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 添加购物车
     *
     * @return
     */
    @RequestMapping(value = "/addCart.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> addCart(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.addCart(user.getId(), productId, count);
    }

    @RequestMapping(value = "/listCart.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.listCart(user.getId());
    }
    /**
     * 购物车更新产品
     *
     * @param session
     * @param count
     * @param productId
     * @return
     */
    @RequestMapping(value = "/updateCart.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> updateCart(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.updateCart(user.getId(), productId, count);
    }

    /**
     * 购物车中删除产品
     *
     * @param session
     * @param productIds
     * @return
     */
    @RequestMapping(value = "/deleteProductCart.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> deleteProductCart(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.deleteProduct(user.getId(), productIds);
    }
    // 全选
    @RequestMapping(value = "/selectAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelectAll(user.getId(),null,Const.Cart.CHECKED);
    }

    // 全反选

    @RequestMapping(value = "/unselectAll.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> unselectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelectAll(user.getId(),null,Const.Cart.UNCHECKED);
    }
    //单独选
    @RequestMapping(value = "/select.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelectAll(user.getId(),productId,Const.Cart.CHECKED);
    }

    // 单独反选
    //单独选
    @RequestMapping(value = "/unselect.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<CartVO> unselect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return cartService.selectOrUnSelectAll(user.getId(),productId,Const.Cart.UNCHECKED);
    }
    // 查询购物车中产品数量

    @RequestMapping(value = "/getCartProduct.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<Integer> getCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createBySuccess(0);
        }
        return cartService.getCartProduct(user.getId());
    }

}

