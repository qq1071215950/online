package com.haojing.service;

import com.haojing.common.ServiceResponse;
import com.haojing.pojo.User;

/**
 * 用户接口
 */
public interface IUserService {
    ServiceResponse<User> login(String username, String password);

    ServiceResponse<String> register(User user);

    ServiceResponse<String> checkVaild(String str, String type);

    ServiceResponse<String> selectQuestion(String username);

    ServiceResponse<String> checkAnswer(String username, String question, String answer);

    ServiceResponse<String> forgetRestPassword(String username, String newPassword, String forgetToken);

    ServiceResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServiceResponse<User> updateInformation(User user);

    ServiceResponse<User> getInformation(Integer userId);

    ServiceResponse checkAdmin(User user);
}
