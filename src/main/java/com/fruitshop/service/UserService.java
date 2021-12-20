//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fruitshop.service;

import com.fruitshop.error.BusinessException;
import com.fruitshop.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);

    UserModel getUserByTelphone(String telphone);

    Boolean isRegisteredByTelphone(String telphone);

    Boolean isRegisteredByEmail(String email);

    void register(UserModel userModel) throws BusinessException;

    UserModel updateUser(UserModel userModel);

    void changePassword(UserModel userModel);

    UserModel validateLoginByTelphone(String telphone, String encrptPassword);

    UserModel validateLoginByEmail(String email, String encrptPassword);
}
