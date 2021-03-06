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

    List<OrderModel> getOrderByStatus(Integer userId, Integer page, Integer status);

    Integer getCount(Integer userId);

    LogisticsModel completeOrder(String id, Integer userId, Integer paymentMethod, Integer addressId);

    void cancelOrder(String id, Integer userId);

    void confirmOrder(String id, Integer userId);

    LogisticsModel completeLogistics(String id, String deliveryCompany, String deliveryNumber, String storeName);

    List<LogisticsModel> getLogistics(String storeName, Integer page);

    List<LogisticsModel> getLogisticsByTitle(String storeName, Integer page, String title);

    List<LogisticsModel> getLogisticsById(String storeName, Integer page, String id);

    List<LogisticsModel> getLogisticsListByAddressId(String storeName, Integer page, String addressName);

    List<LogisticsModel> getLogisticsListByDeliveryName(String storeName, Integer page, String deliveryName);

    String getCompany(String id);

    String getNumber(String id);

    Integer getCountByStoreName(String storeName);
}
