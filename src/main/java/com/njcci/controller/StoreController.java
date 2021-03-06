package com.njcci.controller;

import com.njcci.controller.viewobject.StoreVO;
import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.response.CommonReturnType;
import com.njcci.service.OrderService;
import com.njcci.service.StoreService;
import com.njcci.service.model.LogisticsModel;
import com.njcci.service.model.StoreModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping({"/store"})
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true"
)
public class StoreController extends BaseController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private OrderService orderService;

    public StoreController() {
    }

    @RequestMapping(
            value = {"/login"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType loginByTelphone(@RequestParam(name = "telphone") String telphone, @RequestParam(name = "password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (!StringUtils.isEmpty(telphone) && !StringUtils.isEmpty(password)) {
            StoreModel storeModel = this.storeService.validateLogin(telphone, this.EncodeByMd5(password));
            this.httpServletRequest.getSession().setAttribute("IS_STORE_LOGIN", true);
            this.httpServletRequest.getSession().setAttribute("LOGIN_STORE", storeModel);
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @RequestMapping(
            value = {"/get"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType get() {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            StoreVO storeVO = this.convertFromModel(storeModel);
            return CommonReturnType.create(storeVO);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }

    @RequestMapping(
            value = {"/delivery"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType completeLogistics(@RequestParam(name = "id") String id, @RequestParam(name = "company") String company, @RequestParam(name = "number") String number) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            LogisticsModel logisticsModel = this.orderService.completeLogistics(id, company, number, storeModel.getStoreName());
            //System.out.println(logisticsModel.getAddressId());
            return CommonReturnType.create(logisticsModel);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }

    @RequestMapping(
            value = {"/getlogistics"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getLogistics(@RequestParam(name = "page") Integer page) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            List<LogisticsModel> logisticsModelList = this.orderService.getLogistics(storeModel.getStoreName(), page);
            //System.out.println(storeModel.);
            return CommonReturnType.create(logisticsModelList);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }
    @RequestMapping(
            value = {"/getlogistics_title"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getLogisticsByTitle(@RequestParam(name = "page") Integer page, @RequestParam(name = "title") String title) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            List<LogisticsModel> logisticsModelList = this.orderService.getLogisticsByTitle(storeModel.getStoreName(), page, title);
            return CommonReturnType.create(logisticsModelList);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }
    @RequestMapping(
            value = {"/getlogistics_id"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getLogisticsById(@RequestParam(name = "page") Integer page, @RequestParam(name = "id") String id) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            List<LogisticsModel> logisticsModelList = this.orderService.getLogisticsById(storeModel.getStoreName(), page, id);
            return CommonReturnType.create(logisticsModelList);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }
    @RequestMapping(
            value = {"/getlogistics_addressid"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getLogisticsListByAddressId(@RequestParam(name = "page") Integer page, @RequestParam(name = "addressName") String addressName) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            List<LogisticsModel> logisticsModelList = this.orderService.getLogisticsListByAddressId(storeModel.getStoreName(), page, addressName);
            return CommonReturnType.create(logisticsModelList);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }
    @RequestMapping(
            value = {"/getlogistics_deliveryName"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getLogisticsListByDeliveryName(@RequestParam(name = "page") Integer page, @RequestParam(name = "deliveryName") String deliveryName) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            List<LogisticsModel> logisticsModelList = this.orderService.getLogisticsListByDeliveryName(storeModel.getStoreName(), page, deliveryName);
            return CommonReturnType.create(logisticsModelList);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }
    @RequestMapping(
            value = {"/getcount"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getLogisticsCount() {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            Integer count = this.orderService.getCountByStoreName(storeModel.getStoreName());
            return CommonReturnType.create(count);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "????????????????????????");
        }
    }

    @RequestMapping(
            value = {"/getotp"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone) {
        Boolean isRegistered = this.storeService.isRegisteredByTelphone(telphone);
        if (isRegistered) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("?????????????????????"));
        } else {
            this.sendOtpCode(telphone);
            return CommonReturnType.create((Object)null);
        }
    }

    @RequestMapping(
            value = {"/register"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone, @RequestParam(name = "otpCode") String otpCode, @RequestParam(name = "storeName") String storeName, @RequestParam(name = "address") String address, @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "?????????????????????");
        } else if (this.storeService.isRegisterByStoreName(storeName)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "????????????????????????");
        } else {
            StoreModel storeModel = new StoreModel();
            storeModel.setTelphone(telphone);
            storeModel.setStoreName(storeName);
            storeModel.setAddress(address);
            storeModel.setEncrptPassword(this.EncodeByMd5(password));
            storeModel.setCreateTime(new Date());
            storeModel.setUpdateTime(new Date());
            this.storeService.register(storeModel);
            return CommonReturnType.create((Object)null);
        }
    }

    private void sendOtpCode(String telphone) {
        Random random = new Random();
        int randomInt = random.nextInt(8999);
        randomInt += 1000;
        String otpCode = String.valueOf(randomInt);
        this.httpServletRequest.getSession().setAttribute(telphone, otpCode);
        System.out.println("telphone = " + telphone + "& otpCode = " + otpCode);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException {

        // ????????????MD5??????????????????
        MessageDigest md = MessageDigest.getInstance("MD5");
        // ??????md5??????
        md.update(str.getBytes());
        // digest()??????????????????md5 hash??????????????????8?????????????????????md5 hash??????16??????hex?????????????????????8????????????
        // BigInteger????????????8????????????????????????16???hex??????????????????????????????????????????????????????hash???
        //??????byte??????????????????????????????2????????????????????????2???8????????????16???2?????????
        //return new BigInteger(1, md.digest()).toString(16);

        return new BigInteger(1, md.digest()).toString(16);
    }

    private StoreVO convertFromModel(StoreModel storeModel) {
        if (storeModel == null) {
            return null;
        } else {
            StoreVO storeVO = new StoreVO();
            BeanUtils.copyProperties(storeModel, storeVO);
            return storeVO;
        }
    }
}
