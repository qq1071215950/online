package com.haojing.controller.admin;

import com.haojing.common.Const;
import com.haojing.common.ServiceResponse;
import com.haojing.pojo.User;
import com.haojing.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session){
        ServiceResponse<User> response = userService.login(username,password);
        if (response.isSuccess()){
            User user =response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else {
                return ServiceResponse.createByErrorMessage("不是管理员无法登录");
            }
        }
        return response;
    }
}
