package com.njcci.controller;

import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.response.CommonReturnType;
import com.njcci.service.OrderService;
import com.njcci.service.StoreService;
import com.njcci.service.model.LogisticsModel;
import com.njcci.service.model.StoreModel;
import com.njcci.controller.viewobject.StoreVO;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
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
            return CommonReturnType.create(logisticsModel);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
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
            return CommonReturnType.create(logisticsModelList);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
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
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
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
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("该手机号已注册"));
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
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码错误");
        } else if (this.storeService.isRegisterByStoreName(storeName)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商铺名称已被注册");
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

        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(str.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
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
