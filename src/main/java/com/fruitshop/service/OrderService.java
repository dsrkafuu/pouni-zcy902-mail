//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fruitshop.service;

import com.fruitshop.service.model.LogisticsModel;
import com.fruitshop.service.model.OrderModel;
import java.util.List;

public interface OrderService {
    OrderModel getOrder(String id);

    OrderModel createOrder(Integer itemId, Integer userId, Integer amount);

    OrderModel createOrderFromCart(Integer cartId, Integer userId);

    List<OrderModel> getOrders(Integer userId, Integer page);

    Integer getCount(Integer userId);

    LogisticsModel completeOrder(String id, Integer userId, Integer paymentMethod, Integer addressId);

    void cancelOrder(String id, Integer userId);

    LogisticsModel completeLogistics(String id, String deliveryCompany, String deliveryNumber, String storeName);

    List<LogisticsModel> getLogistics(String storeName, Integer page);

    Integer getCountByStoreName(String storeName);
}
