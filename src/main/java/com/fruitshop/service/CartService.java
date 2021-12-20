package com.fruitshop.service;

import com.fruitshop.service.model.CartModel;

import java.util.List;

public interface CartService {
    //加入购物车
    void addToCart(Integer itemId, Integer userId, Integer amount);

    //删除
    void deleteFromCart(Integer id, Integer userId);

    //更新
    CartModel updateCart(Integer id, Integer amount, Integer userId);

    //列表
    List<CartModel> list(Integer userId);

    CartModel getCart(Integer id);
}
