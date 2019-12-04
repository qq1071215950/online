package com.haojing.service;

import com.github.pagehelper.PageInfo;
import com.haojing.common.ServiceResponse;
import com.haojing.pojo.Product;
import com.haojing.vo.ProductDetailVO;

public interface IProductService {

    /**
     * 更新或者添加产品
     * @param product
     * @return
     */
    ServiceResponse saveOrupdateProduct(Product product);

    /**
     * 产品的上下线
     * @param productId
     * @param status
     * @return
     */
    ServiceResponse<String> setProductStatus(Integer productId, Integer status);

    /**
     * 获取产品详情
     * @param productId
     * @return
     */
    ServiceResponse<ProductDetailVO> manageProductDEtail(Integer productId);

    /**
     * 获取商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServiceResponse<PageInfo> getProductList(int pageNum, int pageSize);

    /**
     * 后台按条件查询商品
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServiceResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    ServiceResponse<ProductDetailVO> getProductDetail(Integer productId);

    ServiceResponse<PageInfo> getproductBykeywordCategoryId(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);

    }
