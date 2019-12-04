package com.haojing.service;

import com.github.pagehelper.PageInfo;
import com.haojing.common.ServiceResponse;
import com.haojing.pojo.Shipping;

public interface IShippingService {

    ServiceResponse add(Integer userId, Shipping shipping);

    ServiceResponse<String> delete(Integer userId, Integer shippingId);

    ServiceResponse<String> update(Integer userId, Shipping shipping );

    ServiceResponse<Shipping> select(Integer userId, Integer shippingId);

    ServiceResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);

}
