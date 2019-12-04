package com.haojing.controller.portal;

import com.haojing.common.Const;
import com.haojing.common.ResponseCode;
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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService userService;
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> login(String username, String password, HttpSession session){
        ServiceResponse<User> response = userService.login(username,password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> logOut(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServiceResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> register(User user){
        return userService.register(user);
    }

    @RequestMapping(value = "checkVaild.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> checkVaild(String str, String type){
        return userService.checkVaild(str,type);
    }

    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
           return ServiceResponse.createBySuccess(user);
        }
        return ServiceResponse.createByErrorMessage("用户未登录，无法获取用户信息");
    }

    @RequestMapping(value = "forgetGetQuestion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetGetQuestion(String username){
        return userService.selectQuestion(username);
    }

    @RequestMapping(value = "forgetCheckAnswer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return  userService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forgetRestPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> forgetRestPassword(String username, String newPassword, String forgetToken){
        return userService.forgetRestPassword(username,newPassword,forgetToken);
    }

    @RequestMapping(value = "resetPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        User user = (User) session.getAttribute(Const.USERNAME);
        if (user == null){
            ServiceResponse.createByErrorMessage("用户未登录");
        }
        return userService.resetPassword(passwordOld, passwordNew, user);
    }

    @RequestMapping(value = "updataInformation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> updataInformation(HttpSession session, User user){
        User curruser = (User) session.getAttribute(Const.USERNAME);
        if (curruser == null){
            ServiceResponse.createByErrorMessage("用户未登录");
        }
        user.setId(curruser.getId());
        ServiceResponse<User> response = userService.updateInformation(user);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return  response;
    }

    @RequestMapping(value = "getInformation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServiceResponse<User> getInformation(HttpSession session){
        User curruser = (User) session.getAttribute(Const.USERNAME);
        if (curruser == null){
            ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登陆");
        }
        return userService.getInformation(curruser.getId());
    }
}
