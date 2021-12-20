package com.fruitshop.controller;

import com.fruitshop.error.BusinessException;
import com.fruitshop.error.EmBusinessError;
import com.fruitshop.response.CommonReturnType;
import com.fruitshop.service.AddressService;
import com.fruitshop.service.model.AddressModel;
import com.fruitshop.service.model.UserModel;
import com.fruitshop.controller.viewobject.AddressVO;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/address"})
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true",
        maxAge = 3600L
)
public class
AddressController extends BaseController {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private AddressService addressService;

    public AddressController() {
    }

    @RequestMapping(
            value = {"/get"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType get(@RequestParam(name = "id") Integer id) {
        AddressModel addressModel = this.addressService.get(id);
        AddressVO addressVO = this.convertFromModel(addressModel);
        return CommonReturnType.create(addressVO);
    }

    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType createAddress(@RequestParam(name = "addresseeName") String addresseeName, @RequestParam(name = "address") String address, @RequestParam(name = "postcode") String postcode, @RequestParam(name = "addresseeTelphone") String addresseeTelphone) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            AddressModel addressModel = new AddressModel();
            addressModel.setAddresseeName(addresseeName);
            addressModel.setAddress(address);
            addressModel.setPostcode(postcode);
            addressModel.setAddresseeTelphone(addresseeTelphone);
            addressModel.setUserId(userModel.getId());
            List<AddressModel> addressModelList = this.addressService.list(userModel.getId());
            if (addressModelList != null && addressModelList.size() != 0) {
                addressModel.setStatus(2);
            } else {
                addressModel.setStatus(1);
            }

            AddressModel addressModel1 = this.addressService.create(addressModel);
            AddressVO addressVO = this.convertFromModel(addressModel1);
            return CommonReturnType.create(addressVO);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/set"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType setMainAddress(@RequestParam(name = "id") Integer id) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            this.addressService.setAddress(id, userModel.getId());
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/list"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType listAddress() {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            List<AddressModel> addressModelList = this.addressService.list(userModel.getId());
            if (addressModelList == null) {
                return CommonReturnType.create((Object)null);
            } else {
                List<AddressVO> addressVOList = (List)addressModelList.stream().map((addressModel) -> {
                    AddressVO addressVO = this.convertFromModel(addressModel);
                    return addressVO;
                }).collect(Collectors.toList());
                return CommonReturnType.create(addressVOList);
            }
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    private AddressVO convertFromModel(AddressModel addressModel) {
        if (addressModel == null) {
            return null;
        } else {
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressModel, addressVO);
            addressVO.setEncrptTelphone(this.getEncrptTelphone(addressModel.getAddresseeTelphone()));
            return addressVO;
        }
    }

    private String getEncrptTelphone(String telphone) {
        return telphone == null ? null : telphone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}