package com.haojing.controller.admin;

import com.haojing.common.Const;
import com.haojing.common.ResponseCode;
import com.haojing.common.ServiceResponse;
import com.haojing.pojo.User;
import com.haojing.service.ICategoryService;
import com.haojing.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = "addCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = " 0" ) int parentId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return categoryService.addCategory(categoryName, parentId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping(value = "setCategoryName.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse setCategoryName(HttpSession session, String categoryName, Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return categoryService.updateCategoryName(categoryId,categoryName);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }
    @RequestMapping(value = "getChildenParalieCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse getChildenParalieCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return categoryService.getChildenParalieCategory(categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping(value = "getCategoryAndDeepChildrenCategory.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if (userService.checkAdmin(user).isSuccess()){
            return categoryService.selectCategoryAndChildrenById(categoryId);
        }else {
            return ServiceResponse.createByErrorMessage("无权限操作");
        }
    }

}
