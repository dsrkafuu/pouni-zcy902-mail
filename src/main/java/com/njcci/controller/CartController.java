package com.njcci.controller;

import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.response.CommonReturnType;
import com.njcci.service.CartService;
import com.njcci.service.model.CartModel;
import com.njcci.service.model.UserModel;
import com.njcci.controller.viewobject.CartVO;

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
@RequestMapping({"/cart"})
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true"
)
public class CartController extends BaseController {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private CartService cartService;

    public CartController() {
    }

    @RequestMapping(
            value = {"add"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType addToCart(@RequestParam(name = "itemId") Integer itemId, @RequestParam(name = "amount") Integer amount) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            this.cartService.addToCart(itemId, userModel.getId(), amount);
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"delete"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType deleteFromCart(@RequestParam(name = "id") Integer id) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            this.cartService.deleteFromCart(id, userModel.getId());
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"list"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType list() {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            List<CartModel> cartModelList = this.cartService.list(userModel.getId());
            if (cartModelList == null) {
                return CommonReturnType.create((Object)null);
            } else {
                List<CartVO> cartVOList = (List)cartModelList.stream().map((cartModel) -> {
                    CartVO cartVO = this.convertFromModel(cartModel);
                    return cartVO;
                }).collect(Collectors.toList());
                return CommonReturnType.create(cartVOList);
            }
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"getnum"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getNum() {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            List<CartModel> cartModelList = this.cartService.list(userModel.getId());
            return cartModelList != null && cartModelList.size() != 0 ? CommonReturnType.create(cartModelList.size()) : CommonReturnType.create(0);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"update"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType update(@RequestParam(name = "id") Integer id, @RequestParam(name = "amount") Integer amount) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            CartModel cartModel = this.cartService.updateCart(id, amount, userModel.getId());
            CartVO cartVO = this.convertFromModel(cartModel);
            return CommonReturnType.create(cartVO);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    private CartVO convertFromModel(CartModel cartModel) {
        if (cartModel == null) {
            return null;
        } else {
            CartVO cartVO = new CartVO();
            BeanUtils.copyProperties(cartModel, cartVO);
            return cartVO;
        }
    }
}