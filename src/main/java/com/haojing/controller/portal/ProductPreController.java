package com.haojing.controller.portal;

import com.github.pagehelper.PageInfo;
import com.haojing.common.ServiceResponse;
import com.haojing.service.IProductService;
import com.haojing.vo.ProductDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/product")
public class ProductPreController {

    @Autowired
    private IProductService productService;

    @RequestMapping(value = "/detail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<ProductDetailVO> detail(Integer productId){
        return productService.getProductDetail(productId);
    }

    @RequestMapping(value = "/list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<PageInfo> list(@RequestParam(value = "keyword",required = false) String keyword,
                                          @RequestParam(value = "categoryId" ,required = false) Integer categoryId,
                                          @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.getproductBykeywordCategoryId(keyword,categoryId,pageNum,pageSize,orderBy);

    }

}
