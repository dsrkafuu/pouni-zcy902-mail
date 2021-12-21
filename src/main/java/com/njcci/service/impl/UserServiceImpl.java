//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.service.impl;

import com.njcci.dao.UserDOMapper;
import com.njcci.dao.UserPasswordDOMapper;
import com.njcci.dataobject.UserDO;
import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.service.UserService;
import com.njcci.dataobject.UserPasswordDO;
import com.njcci.service.model.UserModel;
import com.njcci.validator.ValidationResult;
import com.njcci.validator.ValidatorImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    public UserServiceImpl() {
    }

    public UserModel getUserById(Integer id) {
        UserDO userDO = this.userDOMapper.selectByPrimaryKey(id);
        if (userDO == null) {
            return null;
        } else {
            UserPasswordDO userPasswordDO = this.userPasswordDOMapper.selectByUserId(userDO.getId());
            return this.convertFromDataObject(userDO, userPasswordDO);
        }
    }

    public UserModel getUserByTelphone(String telphone) {
        UserDO userDO = this.userDOMapper.selectByTelphone(telphone);
        UserPasswordDO userPasswordDO = this.userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = this.convertFromDataObject(userDO, userPasswordDO);
        return userModel;
    }

    public Boolean isRegisteredByTelphone(String telphone) {
        UserDO userDO = this.userDOMapper.selectByTelphone(telphone);
        return userDO != null ? true : false;
    }

    public Boolean isRegisteredByEmail(String email) {
        UserDO userDO = this.userDOMapper.selectByEmail(email);
        return userDO != null ? true : false;
    }

    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        } else if (!this.isMobiPhoneNum(userModel.getTelphone())) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号不合法");
        } else {
            ValidationResult validationResult = this.validator.validate(userModel);
            if (validationResult.isHasErrors()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
            } else {
                UserDO userDO = this.convertFromModel(userModel);

                try {
                    this.userDOMapper.insertSelective(userDO);
                } catch (DuplicateKeyException var5) {
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号已注册");
                }

                userModel.setId(userDO.getId());
                UserPasswordDO userPasswordDO = this.convertPasswordFromModel(userModel);
                this.userPasswordDOMapper.insertSelective(userPasswordDO);
            }
        }
    }

    @Transactional
    public UserModel updateUser(UserModel userModel) {
        UserDO userDO = this.convertFromModel(userModel);
        int num = this.userDOMapper.updateByPrimaryKeySelective(userDO);
        if (num != 1) {
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        } else {
            return userModel;
        }
    }

    @Transactional
    public void changePassword(UserModel userModel) {
        UserPasswordDO userPasswordDO = this.convertPasswordFromModel(userModel);
        this.userPasswordDOMapper.updateByUserIdSelective(userPasswordDO);
    }

    public UserModel validateLoginByTelphone(String telphone, String encrptPassword) {
        UserDO userDO = this.userDOMapper.selectByTelphone(telphone);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        } else {
            UserPasswordDO userPasswordDO = this.userPasswordDOMapper.selectByUserId(userDO.getId());
            UserModel userModel = this.convertFromDataObject(userDO, userPasswordDO);
            if (!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())) {
                throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
            } else {
                return userModel;
            }
        }
    }

    public UserModel validateLoginByEmail(String email, String encrptPassword) {
        UserDO userDO = this.userDOMapper.selectByEmail(email);
        if (userDO == null) {
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        } else {
            UserPasswordDO userPasswordDO = this.userPasswordDOMapper.selectByUserId(userDO.getId());
            UserModel userModel = this.convertFromDataObject(userDO, userPasswordDO);
            if (!StringUtils.equals(encrptPassword, userModel.getEncrptPassword())) {
                throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
            } else {
                return userModel;
            }
        }
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        } else {
            UserPasswordDO userPasswordDO = new UserPasswordDO();
            userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
            userPasswordDO.setUserId(userModel.getId());
            userPasswordDO.setCreateTime(userModel.getCreateTime());
            userPasswordDO.setUpdateTime(userModel.getUpdateTime());
            return userPasswordDO;
        }
    }

    private UserDO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        } else {
            UserDO userDO = new UserDO();
            BeanUtils.copyProperties(userModel, userDO);
            return userDO;
        }
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        } else {
            UserModel userModel = new UserModel();
            BeanUtils.copyProperties(userDO, userModel);
            if (userPasswordDO != null) {
                userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
            }

            return userModel;
        }
    }

    private boolean isMobiPhoneNum(String telNum) {
        String regex = "^1[3456789]\\d{9}$";
        Pattern p = Pattern.compile(regex, 2);
        Matcher m = p.matcher(telNum);
        return m.matches();
    }
}
