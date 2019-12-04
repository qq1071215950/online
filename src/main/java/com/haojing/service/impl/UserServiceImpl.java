package com.haojing.service.impl;

import com.haojing.common.Const;
import com.haojing.common.ServiceResponse;
import com.haojing.common.TokenCache;
import com.haojing.dao.UserMapper;
import com.haojing.pojo.User;
import com.haojing.service.IUserService;
import com.haojing.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public ServiceResponse<User> login(String username, String password) {
        int resultCount =userMapper.checkUsername(username);
        if (resultCount == 0){
            return ServiceResponse.createByErrorMessage("用户名不存在");
        }
        // todo md5密码加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if (user == null){
            return ServiceResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServiceResponse<String> register(User user) {
        int resultCount = userMapper.checkUsername(user.getUsername());
        if (resultCount > 0){
            return ServiceResponse.createByErrorMessage("用户名已经存在");
        }
        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0){
            return ServiceResponse.createByErrorMessage("邮箱已经存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if (resultCount == 0){
            return ServiceResponse.createByErrorMessage("注册失败");
        }
        return ServiceResponse.createBySuccess("注册成功");
    }

    public ServiceResponse<String> checkVaild(String str, String type){
        if (StringUtils.isNotBlank(type)){
            if (Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0){
                    return ServiceResponse.createByErrorMessage("用户名已经存在");
                }
            }
            if (Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0){
                    return ServiceResponse.createByErrorMessage("邮箱已经存在");
                }
            }
        }else {
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        return ServiceResponse.createBySuccess("校验成功");
    }

    public ServiceResponse<String> selectQuestion(String username){
        ServiceResponse validReponse = this.checkVaild(username, Const.USERNAME);
        if (validReponse.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)){
            return ServiceResponse.createBySuccess(question);
        }
        return ServiceResponse.createByErrorMessage("找回密码的问题为空");
    }

    public ServiceResponse<String> checkAnswer(String username, String question, String answer){
        int result = userMapper.checkAnswer(username, question, answer);
        if (result > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServiceResponse.createBySuccess(forgetToken);
        }
        return ServiceResponse.createByErrorMessage("问题的答案错误");
    }

    public ServiceResponse<String> forgetRestPassword(String username, String newPassword, String forgetToken){
        if (StringUtils.isBlank(forgetToken)){
            return ServiceResponse.createByErrorMessage("参数错误");
        }
        ServiceResponse validReponse = this.checkVaild(username, Const.USERNAME);
        if (validReponse.isSuccess()){
            return ServiceResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if (StringUtils.isBlank(token)){
            return ServiceResponse.createByErrorMessage("token 无效或者过期");
        }
        if (StringUtils.equals(forgetToken, token)){
            String mdsPassword = MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username, mdsPassword);
            if (rowCount > 0) {
                return ServiceResponse.createBySuccessMessage("修改密码成功");
            }else {
                return ServiceResponse.createByErrorMessage("token失效");
            }
        }
        return ServiceResponse.createByErrorMessage("修改密码失败");
    }

    public ServiceResponse<String> resetPassword(String passwordOld, String passwordNew, User user){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0){
            return ServiceResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updataCount = userMapper.updateByPrimaryKeySelective(user);
        if (updataCount > 0){
            return ServiceResponse.createBySuccessMessage("密码更新成功");
        }
        return ServiceResponse.createByErrorMessage("密码更新失败");
    }

    public ServiceResponse<User> updateInformation(User user){
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if (resultCount > 0){
            return ServiceResponse.createByErrorMessage("email已经存在，请重新填写");
        }
        User dataUser = new User();
        dataUser.setId(user.getId());
        dataUser.setEmail(user.getEmail());
        dataUser.setAnswer(user.getAnswer());
        dataUser.setQuestion(user.getQuestion());
        dataUser.setPhone(user.getPhone());
        int updateCount = userMapper.updateByPrimaryKeySelective(dataUser);
        if (updateCount > 0){
            return ServiceResponse.createBySuccess("更新信息成功",dataUser);
        }
        return ServiceResponse.createByErrorMessage("更新信息失败");
    }

    public ServiceResponse<User> getInformation(Integer userId){
       User user = userMapper.selectByPrimaryKey(userId);
        if (user == null){
            return ServiceResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServiceResponse.createBySuccess(user);
    }

    public ServiceResponse checkAdmin(User user){
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServiceResponse.createBySuccess();
        }
        return ServiceResponse.createByError();
    }
}
