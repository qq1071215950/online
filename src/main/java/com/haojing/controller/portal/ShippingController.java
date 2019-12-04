package com.haojing.controller.portal;

import com.github.pagehelper.PageInfo;
import com.haojing.common.Const;
import com.haojing.common.ResponseCode;
import com.haojing.common.ServiceResponse;
import com.haojing.pojo.Shipping;
import com.haojing.pojo.User;
import com.haojing.service.IShippingService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @RequestMapping(value = "/add.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse add(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return shippingService.add(user.getId(),shipping);
    }

    @RequestMapping(value = "/delete.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse delete(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return shippingService.delete(user.getId(),shippingId);
    }

    // update
    @RequestMapping(value = "/update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse update(HttpSession session, Shipping shipping){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return shippingService.update(user.getId(),shipping);
    }

    @RequestMapping(value = "/select.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<Shipping> select(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return shippingService.select(user.getId(),shippingId);
    }

    @RequestMapping(value = "/list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize, HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return shippingService.list(user.getId(),pageNum,pageSize);
    }
}
