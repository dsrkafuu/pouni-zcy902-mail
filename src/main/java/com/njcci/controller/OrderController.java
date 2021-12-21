package com.njcci.controller;

import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.response.CommonReturnType;
import com.njcci.service.CartService;
import com.njcci.service.OrderService;
import com.njcci.service.model.LogisticsModel;
import com.njcci.service.model.OrderModel;
import com.njcci.service.model.UserModel;
import com.njcci.controller.viewobject.OrderVO;

import java.util.ArrayList;
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
@RequestMapping({"/order"})
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true",
        maxAge = 3600L
)
public class OrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    public OrderController() {
    }

    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId, @RequestParam(name = "amount") Integer amount) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            OrderModel orderModel = this.orderService.createOrder(itemId, userModel.getId(), amount);
            OrderVO orderVO = this.convertFromModel(orderModel);
            return CommonReturnType.create(orderVO);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/createfromcart"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType createOrderFromCart(@RequestParam(name = "id") String id) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            String[] cartIds = id.split(",");
            List<OrderModel> orderModelList = new ArrayList();

            for(int i = 0; i < cartIds.length; ++i) {
                OrderModel orderModel = this.orderService.createOrderFromCart(Integer.parseInt(cartIds[i]), userModel.getId());
                this.cartService.deleteFromCart(Integer.parseInt(cartIds[i]), userModel.getId());
                orderModelList.add(orderModel);
            }

            List<OrderVO> orderVOList = (List)orderModelList.stream().map((orderModelx) -> {
                OrderVO orderVO = this.convertFromModel(orderModelx);
                return orderVO;
            }).collect(Collectors.toList());
            return CommonReturnType.create(orderVOList);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/list"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getOrders(@RequestParam(name = "page") Integer page) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            List<OrderModel> orderModelList = this.orderService.getOrders(userModel.getId(), page);
            List<OrderVO> orderVOList = (List)orderModelList.stream().map((orderModel) -> {
                OrderVO orderVO = this.convertFromModel(orderModel);
                return orderVO;
            }).collect(Collectors.toList());
            return CommonReturnType.create(orderVOList);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/count"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getCount() {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            Integer count = this.orderService.getCount(userModel.getId());
            return CommonReturnType.create(count);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/getbyid"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getOrderById(String id) {
        OrderModel orderModel = this.orderService.getOrder(id);
        OrderVO orderVO = this.convertFromModel(orderModel);
        return CommonReturnType.create(orderVO);
    }

    @RequestMapping(
            value = {"/complete"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType completeOrder(@RequestParam(name = "id") String id, @RequestParam(name = "paymentMethod") Integer paymentMethod, @RequestParam(name = "addressId") Integer addressId) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            LogisticsModel logisticsModel = this.orderService.completeOrder(id, userModel.getId(), paymentMethod, addressId);
            return CommonReturnType.create(logisticsModel);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    @RequestMapping(
            value = {"/cancel"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType cancelOrder(@RequestParam(name = "id") String id) {
        Boolean isLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if (isLogin != null && isLogin) {
            UserModel userModel = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
            this.orderService.cancelOrder(id, userModel.getId());
            return CommonReturnType.create((Object)null);
        } else {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
    }

    private OrderVO convertFromModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        } else {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orderModel, orderVO);
            return orderVO;
        }
    }
}
