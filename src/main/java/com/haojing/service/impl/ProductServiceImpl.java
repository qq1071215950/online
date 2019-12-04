package com.haojing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.haojing.common.Const;
import com.haojing.common.ResponseCode;
import com.haojing.common.ServiceResponse;
import com.haojing.dao.CategoryMapper;
import com.haojing.dao.ProductMapper;
import com.haojing.pojo.Category;
import com.haojing.pojo.Product;
import com.haojing.service.ICategoryService;
import com.haojing.service.IProductService;
import com.haojing.utils.DateTimeUtil;
import com.haojing.utils.PropertiesUtil;
import com.haojing.vo.ProductDetailVO;
import com.haojing.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService categoryService;
    public ServiceResponse saveOrupdateProduct(Product product){
        if (product != null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0){
                    product.setSubImages(subImageArray[0]);
                }
            }
            if (product.getId() != null){
               int rowCount = productMapper.updateByPrimaryKeySelective(product);
               if (rowCount > 0){
                   return ServiceResponse.createBySuccessMessage("更新产品成功");
               }else {
                   return ServiceResponse.createByErrorMessage("更新产品失败");
               }
            }else {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0){
                    return ServiceResponse.createBySuccessMessage("新增产品成功");
                }else {
                    return ServiceResponse.createByErrorMessage("新增产品失败");
                }
            }
        }
        return ServiceResponse.createByErrorMessage("新增或者更新产品参数不对");
    }

    public ServiceResponse<String> setProductStatus(Integer productId, Integer status){
        if (productId == null || status == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount  = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0){
            return ServiceResponse.createBySuccessMessage("修改成功");
        }
        return ServiceResponse.createByErrorMessage("修改失败");
    }

    public ServiceResponse<ProductDetailVO> manageProductDEtail(Integer productId){
        if (productId == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServiceResponse.createByErrorMessage("产品已经下架或者删除");
        }
        // 返回vo对象 对象转换
        ProductDetailVO productDetailVO = assemableProductDetailVO(product);
        return ServiceResponse.createBySuccess(productDetailVO);
    }

    public ServiceResponse<PageInfo> getProductList(int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.slectList();
        List<ProductListVO> productListVOS = Lists.newArrayList();
        for (Product product: products){
            ProductListVO productListVO = assemableProductListVO(product);
            productListVOS.add(productListVO);
        }
        PageInfo pageResult = new PageInfo(products);
        pageResult.setList(productListVOS);
        return ServiceResponse.createBySuccess(pageResult);

    }
    private ProductDetailVO assemableProductDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setName(product.getName());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());

        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","ftp://192.168.1.51/"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            productDetailVO.setParentCategoryId(0);
        }else{
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        // 格式化时间
        productDetailVO.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return  productDetailVO;
    }
    private ProductListVO assemableProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setStatus(product.getStatus());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setPrice(product.getPrice());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","ftp://192.168.1.51/"));
        return productListVO;
    }

    public ServiceResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectProductByNameAndId(productName,productId);
        List<ProductListVO> productListVOS = Lists.newArrayList();
        for (Product product: productList){
            ProductListVO productListVO = assemableProductListVO(product);
            productListVOS.add(productListVO);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOS);
        return ServiceResponse.createBySuccess(pageResult);
    }

    public ServiceResponse<ProductDetailVO> getProductDetail(Integer productId){
        if (productId == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServiceResponse.createByErrorMessage("产品已经下架或者删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SAIL.getCode()){
            return ServiceResponse.createByErrorMessage("产品已经下架或者删除");
        }
        // 返回vo对象 对象转换
        ProductDetailVO productDetailVO = assemableProductDetailVO(product);
        return ServiceResponse.createBySuccess(productDetailVO);
    }

    public ServiceResponse<PageInfo> getproductBykeywordCategoryId(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy){
        // 参数校验
        if (StringUtils.isBlank(keyword) && categoryId == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();
        if (categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)){
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOS = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOS);
                return ServiceResponse.createBySuccess(pageInfo);
            }
            categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);
        if (StringUtils.isNotBlank(orderBy)){
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+""+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword) ? null:keyword,categoryIdList.size() == 0 ? null:categoryIdList);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        for (Product product:productList){
            ProductListVO productListVO = assemableProductListVO(product);
            productListVOList.add(productListVO);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);
        return ServiceResponse.createBySuccess(pageInfo);
    }
}
