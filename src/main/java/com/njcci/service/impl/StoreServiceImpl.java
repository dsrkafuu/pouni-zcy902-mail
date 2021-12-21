//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.service.impl;

import com.njcci.dao.StoreDOMapper;
import com.njcci.dao.StorePasswordDOMapper;
import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.service.StoreService;
import com.njcci.dataobject.StoreDO;
import com.njcci.dataobject.StorePasswordDO;
import com.njcci.service.model.StoreModel;
import com.njcci.validator.ValidationResult;
import com.njcci.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreDOMapper storeDOMapper;
    @Autowired
    private StorePasswordDOMapper storePasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    public StoreServiceImpl() {
    }

    public StoreModel getStoreByTelphone(String telphone) {
        return null;
    }

    public Boolean isRegisteredByTelphone(String telphone) {
        StoreDO storeDO = this.storeDOMapper.selectByTelphone(telphone);
        return storeDO == null ? false : true;
    }

    public Boolean isRegisterByStoreName(String storeName) {
        StoreDO storeDO = this.storeDOMapper.selectByStoreName(storeName);
        return storeDO == null ? false : true;
    }

    public void register(StoreModel storeModel) {
        if (storeModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        } else {
            ValidationResult validationResult = this.validator.validate(storeModel);
            if (validationResult.isHasErrors()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
            } else {
                StoreDO storeDO = this.convertStoreDOFromModel(storeModel);

                try {
                    this.storeDOMapper.insertSelective(storeDO);
                } catch (DuplicateKeyException var5) {
                    throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号已注册");
                }

                storeModel.setId(storeDO.getId());
                StorePasswordDO storePasswordDO = this.convertStorePasswordDOFromModel(storeModel);
                this.storePasswordDOMapper.insertSelective(storePasswordDO);
            }
        }
    }

    public StoreModel validateLogin(String telphone, String encrptPassword) {
        StoreDO storeDO = this.storeDOMapper.selectByTelphone(telphone);
        if (storeDO == null) {
            throw new BusinessException(EmBusinessError.STORE_NOT_EXIST);
        } else {
            StorePasswordDO storePasswordDO = this.storePasswordDOMapper.selectByStoreId(storeDO.getId());
            if (!StringUtils.equals(storePasswordDO.getEncrptPassword(), encrptPassword)) {
                throw new BusinessException(EmBusinessError.STORE_LOGIN_FAIL);
            } else {
                StoreModel storeModel = this.convertFromDO(storeDO, storePasswordDO);
                return storeModel;
            }
        }
    }

    private StoreModel convertFromDO(StoreDO storeDO, StorePasswordDO storePasswordDO) {
        if (storeDO != null && storePasswordDO != null) {
            StoreModel storeModel = new StoreModel();
            BeanUtils.copyProperties(storeDO, storeModel);
            storeModel.setEncrptPassword(storePasswordDO.getEncrptPassword());
            return storeModel;
        } else {
            return null;
        }
    }

    private StoreDO convertStoreDOFromModel(StoreModel storeModel) {
        if (storeModel == null) {
            return null;
        } else {
            StoreDO storeDO = new StoreDO();
            BeanUtils.copyProperties(storeModel, storeDO);
            return storeDO;
        }
    }

    private StorePasswordDO convertStorePasswordDOFromModel(StoreModel storeModel) {
        if (storeModel == null) {
            return null;
        } else {
            StorePasswordDO storePasswordDO = new StorePasswordDO();
            storePasswordDO.setStoreId(storeModel.getId());
            storePasswordDO.setEncrptPassword(storeModel.getEncrptPassword());
            storePasswordDO.setCreateTime(storeModel.getCreateTime());
            storePasswordDO.setUpdateTime(storeModel.getUpdateTime());
            return storePasswordDO;
        }
    }
}
