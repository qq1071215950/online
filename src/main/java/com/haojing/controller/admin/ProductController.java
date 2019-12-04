package com.haojing.controller.admin;

import com.google.common.collect.Maps;
import com.haojing.common.Const;
import com.haojing.common.ResponseCode;
import com.haojing.common.ServiceResponse;
import com.haojing.pojo.Product;
import com.haojing.pojo.User;
import com.haojing.service.IFileService;
import com.haojing.service.IProductService;
import com.haojing.service.IUserService;
import com.haojing.utils.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IFileService fileService;

    /**
     * 新增或者更新产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "/saveProduct.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse saveProduct(HttpSession session, Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return productService.saveOrupdateProduct(product);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 产品的上下线
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "/setProductStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse setProductStatus(HttpSession session, Integer productId, Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return productService.setProductStatus(productId,status);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 查看产品的详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/getProductDetail.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse getProductDetail(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return productService.manageProductDEtail(productId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取产品列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/getProductList.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse getProductList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return productService.getProductList(pageNum,pageSize);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 产品查找
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/productSearch.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse productSearch(HttpSession session, String productName, Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return productService.searchProduct(productName,productId,pageNum,pageSize);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping(value = "/upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServiceResponse.createBySuccess(fileMap);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping(value = "/richtextImgUpload.do",method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
       Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        if (userService.checkAdmin(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file,path);
            if (StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("uri","上传成功");
            resultMap.put("url",url);
            response.addHeader("Access","x-file-name");
            return resultMap;
        }else {
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }
}
