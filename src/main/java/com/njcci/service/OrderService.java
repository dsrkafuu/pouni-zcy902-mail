//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.service;

import com.njcci.service.model.LogisticsModel;
import com.njcci.service.model.OrderModel;
import java.util.List;

public interface OrderService {
    OrderModel getOrder(String id);

    OrderModel createOrder(Integer itemId, Integer userId, Integer amount);

    OrderModel createOrderFromCart(Integer cartId, Integer userId);

    List<OrderModel> getOrders(Integer userId, Integer page);

    List<OrderModel> getOrderByTitle(Integer userId, Integer page, String title);

    List<OrderModel> getOrderById(Integer userId, Integer page, String id);

    Integer getCount(Integer userId);

    LogisticsModel completeOrder(String id, Integer userId, Integer paymentMethod, Integer addressId);

    void cancelOrder(String id, Integer userId);

    LogisticsModel completeLogistics(String id, String deliveryCompany, String deliveryNumber, String storeName);

    List<LogisticsModel> getLogistics(String storeName, Integer page);

    Integer getCountByStoreName(String storeName);
}
