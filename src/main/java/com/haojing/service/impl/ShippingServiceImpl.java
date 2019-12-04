package com.haojing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.haojing.common.ServiceResponse;
import com.haojing.dao.ShippingMapper;
import com.haojing.pojo.Shipping;
import com.haojing.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServiceResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServiceResponse.createBySuccess("新建地址成功",result);
        }else {
            return ServiceResponse.createByErrorMessage("新建地址失败");
        }
    }

    public ServiceResponse<String> delete(Integer userId, Integer shippingId){
        int rowCount = shippingMapper.deleteByShippingAndUserId(userId,shippingId);
        if (rowCount > 0){
            return ServiceResponse.createBySuccessMessage("删除地址成功");
        }else {
            return ServiceResponse.createByErrorMessage("新建地址失败");
        }
    }

    public ServiceResponse<String> update(Integer userId, Shipping shipping ){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateShipping(shipping);
        if (rowCount > 0){
            return ServiceResponse.createBySuccessMessage("更新地址成功");
        }else {
            return ServiceResponse.createByErrorMessage("更新地址失败");
        }
    }

    public ServiceResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId,shippingId);
        if (shipping == null){
            return ServiceResponse.createByErrorMessage("无法查到");
        }
        return ServiceResponse.createBySuccess("查找成功",shipping);
    }

    public ServiceResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServiceResponse.createBySuccess(pageInfo);
    }
}
