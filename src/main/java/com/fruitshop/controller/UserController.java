package com.fruitshop.controller;

import com.fruitshop.error.BusinessException;
import com.fruitshop.error.EmBusinessError;
import com.fruitshop.response.CommonReturnType;
import com.fruitshop.service.UserService;
import com.fruitshop.service.model.UserModel;
import com.fruitshop.controller.viewobject.UserVO;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
@RequestMapping({"/user"})
@CrossOrigin(
        origins = {"*"},
        maxAge = 3600
)
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    public UserController() {
    }

    @RequestMapping(
            value = {"/loginbytelphone"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType loginByTelphone(@RequestParam(name = "telphone") String telphone, @RequestParam(name = "password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            this.httpServletRequest.getSession().invalidate();
        }
        if (!StringUtils.isEmpty(telphone) && !StringUtils.isEmpty(password)) {
            UserModel userModel = this.userService.validateLoginByTelphone(telphone, this.EncodeByMd5(password));
            this.httpServletRequest.getSession().setAttribute("IS_USER_LOGIN", true);
            this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
            //登录有效时长（单位：秒）
            this.httpServletRequest.getSession().setMaxInactiveInterval(600);
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @RequestMapping(
            value = {"/loginbyemail"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType loginByEmail(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            this.httpServletRequest.getSession().invalidate();
        }

        if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(password)) {
            UserModel userModel = this.userService.validateLoginByEmail(email, this.EncodeByMd5(password));
            this.httpServletRequest.getSession().setAttribute("IS_USER_LOGIN", true);
            this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @RequestMapping(
            value = {"/register"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType register(@RequestParam(name = "telphone") String telphone, @RequestParam(name = "otpCode") String otpCode, @RequestParam(name = "name") String name, @RequestParam(name = "gender") Integer gender, @RequestParam(name = "age") Integer age, @RequestParam(name = "email") String email, @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码错误");
        } else if (this.userService.isRegisteredByEmail(email)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "电子邮箱已注册");
        } else {
            UserModel userModel = new UserModel();
            userModel.setName(name);
            userModel.setAge(age);
            userModel.setGender(gender);
            userModel.setEmail(email);
            userModel.setTelphone(telphone);
            userModel.setEncrptPassword(this.EncodeByMd5(password));
            userModel.setCreateTime(new Date());
            userModel.setUpdateTime(new Date());
            this.userService.register(userModel);
            return CommonReturnType.create((Object)null);
        }
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

    @RequestMapping(
            value = {"/getotp"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getOtp(@RequestParam(name = "telphone") String telphone) {
        if (!this.isMobiPhoneNum(telphone)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "手机号不合法");
        } else {
            Boolean isRegistered = this.userService.isRegisteredByTelphone(telphone);
            if (isRegistered) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("该手机号已注册"));
            } else {
                this.sendOtpCode(telphone);
                return CommonReturnType.create((Object)null);
            }
        }
    }

    @RequestMapping(
            value = {"/get"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getUser() throws BusinessException {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            } else {
                UserVO userVO = this.convertFromModel(userModel);
                return CommonReturnType.create(userVO);
            }
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/logout"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType logout() {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            this.httpServletRequest.getSession().invalidate();
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/update"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType updateUser(@RequestParam(name = "name") String name, @RequestParam(name = "gender") Integer gender, @RequestParam(name = "age") Integer age, @RequestParam(name = "email") String email) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            userModel.setName(name);
            userModel.setAge(age);
            userModel.setGender(gender);
            userModel.setEmail(email);
            userModel.setUpdateTime(new Date());
            UserModel userModel1 = this.userService.updateUser(userModel);
            UserVO userVO = this.convertFromModel(userModel1);
            return CommonReturnType.create(userVO);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/change"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType changePassword(@RequestParam(name = "password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            if (this.EncodeByMd5(password).equals(userModel.getEncrptPassword())) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "与近期密码相同，请重新设置密码");
            } else {
                userModel.setEncrptPassword(this.EncodeByMd5(password));
                userModel.setUpdateTime(new Date());
                this.userService.changePassword(userModel);
                this.httpServletRequest.getSession().invalidate();
                return CommonReturnType.create((Object)null);
            }
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/forgot"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType forgotPassword(@RequestParam(name = "telphone") String telphone) {
        Boolean isRegistered = this.userService.isRegisteredByTelphone(telphone);
        if (!isRegistered) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        } else {
            this.sendOtpCode(telphone);
            return CommonReturnType.create((Object)null);
        }
    }

    @RequestMapping(
            value = {"/check"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType checkOtpCode(@RequestParam(name = "telphone") String telphone, @RequestParam(name = "otpCode") String otpCode) {
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        System.out.println("telphone = " + telphone + "& otpCode = " + inSessionOtpCode);
        if (!StringUtils.equals(inSessionOtpCode, otpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码错误");
        } else {
            UserModel userModel = this.userService.getUserByTelphone(telphone);
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            } else {
                this.httpServletRequest.getSession().setAttribute("IS_USER_CHECKED", true);
                this.httpServletRequest.getSession().setAttribute("CHECKED_USER", userModel);
                return CommonReturnType.create((Object)null);
            }
        }
    }

    @RequestMapping(
            value = {"/reset"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType resetPassword(@RequestParam(name = "password") String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Boolean isChecked = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_CHECKED");
        if (isChecked != null && isChecked) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("CHECKED_USER");
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            } else if (this.EncodeByMd5(password).equals(userModel.getEncrptPassword())) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "与近期密码相同，请重新设置密码");
            } else {
                userModel.setEncrptPassword(this.EncodeByMd5(password));
                userModel.setUpdateTime(new Date());
                this.userService.changePassword(userModel);
                return CommonReturnType.create((Object)null);
            }
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_CHECKED);
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

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        } else {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(userModel, userVO);
            return userVO;
        }
    }

    private boolean isMobiPhoneNum(String telNum) {
        String regex = "^1[3456789]\\d{9}$";
        Pattern p = Pattern.compile(regex, 2);
        Matcher m = p.matcher(telNum);
        return m.matches();
    }
}
