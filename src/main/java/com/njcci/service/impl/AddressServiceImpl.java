//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.service.impl;

import com.njcci.dao.AddressDOMapper;
import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.service.UserService;
import com.njcci.dataobject.AddressDO;
import com.njcci.service.AddressService;
import com.njcci.service.model.AddressModel;
import com.njcci.service.model.UserModel;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressDOMapper addressDOMapper;

    public AddressServiceImpl() {
    }

    public AddressModel get(Integer id) {
        AddressDO addressDO = this.addressDOMapper.selectByPrimaryKey(id);
        AddressModel addressModel = this.convertFromDO(addressDO);
        return addressModel;
    }

    public AddressModel create(AddressModel addressModel) {
        if (addressModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        } else {
            UserModel userModel = this.userService.getUserById(addressModel.getUserId());
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            } else if (!this.checkTelphone(addressModel.getAddresseeTelphone())) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号码不合法");
            } else if (!this.checkPostcode(addressModel.getPostcode())) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "邮编不合法");
            } else {
                AddressDO addressDO = this.convertFromModel(addressModel);
                this.addressDOMapper.insertSelective(addressDO);
                return addressModel;
            }
        }
    }

    public List<AddressModel> list(Integer userId) {
        UserModel userModel = this.userService.getUserById(userId);
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        } else {
            List<AddressDO> addressDOList = this.addressDOMapper.selectByUserId(userId);
            if (addressDOList != null && addressDOList.size() != 0) {
                List<AddressModel> addressModelList = (List)addressDOList.stream().map((addressDO) -> {
                    AddressModel addressModel = this.convertFromDO(addressDO);
                    return addressModel;
                }).collect(Collectors.toList());
                return addressModelList;
            } else {
                return null;
            }
        }
    }

    @Transactional
    public void setAddress(Integer id, Integer userId) {
        AddressDO addressDO = this.addressDOMapper.selectByPrimaryKey(id);
        if (addressDO == null) {
            throw new BusinessException(EmBusinessError.ADDRESS_NOT_EXIST);
        } else if (addressDO.getUserId() != userId) {
            throw new BusinessException(EmBusinessError.ADDRESS_STATUS_ERROR);
        } else {
            int result = this.addressDOMapper.setUsualAddress(userId);
            if (result != 1) {
                throw new BusinessException(EmBusinessError.ADDRESS_STATUS_ERROR);
            } else {
                this.addressDOMapper.setMainAddress(id);
                if (result != 1) {
                    throw new BusinessException(EmBusinessError.ADDRESS_STATUS_ERROR);
                }
            }
        }
    }

    private AddressModel convertFromDO(AddressDO addressDO) {
        if (addressDO == null) {
            return null;
        } else {
            AddressModel addressModel = new AddressModel();
            BeanUtils.copyProperties(addressDO, addressModel);
            return addressModel;
        }
    }

    private AddressDO convertFromModel(AddressModel addressModel) {
        if (addressModel == null) {
            return null;
        } else {
            AddressDO addressDO = new AddressDO();
            BeanUtils.copyProperties(addressModel, addressDO);
            return addressDO;
        }
    }

    private boolean checkTelphone(String telphone) {
        //String regex = "^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        String regex = "^1[3-9]\\d{9}$";
        Pattern p = Pattern.compile(regex, 2);
        Matcher m = p.matcher(telphone);
        return m.matches();
    }

    private boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }
}
