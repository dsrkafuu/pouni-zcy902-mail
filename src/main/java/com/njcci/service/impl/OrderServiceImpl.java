//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.service.impl;

import com.njcci.dao.ItemDOMapper;
import com.njcci.dao.LogisticsDOMapper;
import com.njcci.dao.OrderDOMapper;
import com.njcci.dao.SequenceDOMapper;
import com.njcci.dataobject.LogisticsDO;
import com.njcci.dataobject.OrderDO;
import com.njcci.dataobject.SequenceDO;
import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.service.CartService;
import com.njcci.service.ItemService;
import com.njcci.service.OrderService;
import com.njcci.service.UserService;
import com.njcci.service.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private LogisticsDOMapper logisticsDOMapper;
    @Autowired
    private ItemDOMapper itemDOMapper;

    public OrderServiceImpl() {
    }

    public OrderModel getOrder(String id) {
        OrderDO orderDO = this.orderDOMapper.selectByPrimaryKey(id);
        OrderModel orderModel = this.convertFromDataObject(orderDO);
        return orderModel;
    }

    @Transactional
    public OrderModel createOrder(Integer itemId, Integer userId, Integer amount) {
        ItemModel itemModel = this.itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        } else {
            UserModel userModel = this.userService.getUserById(userId);
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            } else if (amount > 0 && amount < 99) {
                boolean result = this.itemService.decreaseStock(itemId, amount);
                if (!result) {
                    throw new BusinessException(EmBusinessError.ITEM_NOT_ENOUGH);
                } else {
                    OrderModel orderModel = new OrderModel();
                    orderModel.setId(this.generateOrderNo());
                    orderModel.setItemId(itemId);
                    orderModel.setUserId(userId);
                    orderModel.setAmount(amount);
                    orderModel.setItemPrice(itemModel.getPrice());
                    orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
                    orderModel.setCreateTime(new Date());
                    orderModel.setStatus(1);
                    OrderDO orderDO = this.convertFromOrderModer(orderModel);
                    this.orderDOMapper.insertSelective(orderDO);
                    this.itemService.increaseSales(itemId, amount);
                    return orderModel;
                }
            } else {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("数量信息不正确"));
            }
        }
    }

    @Transactional
    public OrderModel createOrderFromCart(Integer cartId, Integer userId) {
        CartModel cartModel = this.cartService.getCart(cartId);
        ItemModel itemModel = this.itemService.getItemById(cartModel.getItemId());
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        } else {
            UserModel userModel = this.userService.getUserById(userId);
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            } else if (userModel.getId() != cartModel.getUserId()) {
                throw new BusinessException(EmBusinessError.USER_STATUS_ERROR);
            } else if (cartModel.getAmount() > 0 && cartModel.getAmount() < 99) {
                boolean result = this.itemService.decreaseStock(cartModel.getItemId(), cartModel.getAmount());
                if (!result) {
                    throw new BusinessException(EmBusinessError.ITEM_NOT_ENOUGH);
                } else {
                    OrderModel orderModel = new OrderModel();
                    orderModel.setId(this.generateOrderNo());
                    orderModel.setItemId(cartModel.getItemId());
                    orderModel.setUserId(userId);
                    orderModel.setAmount(cartModel.getAmount());
                    orderModel.setItemPrice(itemModel.getPrice());
                    orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(cartModel.getAmount())));
                    orderModel.setCreateTime(new Date());
                    orderModel.setStatus(1);
                    OrderDO orderDO = this.convertFromOrderModer(orderModel);
                    this.orderDOMapper.insertSelective(orderDO);
                    this.itemService.increaseSales(cartModel.getItemId(), cartModel.getAmount());
                    return orderModel;
                }
            } else {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("数量信息不正确"));
            }
        }
    }

    public List<OrderModel> getOrders(Integer userId, Integer page) {
        List<OrderDO> orderDOList = this.orderDOMapper.selectByUserId(userId, (page - 1) * 3);
        List<OrderModel> orderModelList = (List)orderDOList.stream().map((orderDO) -> {
            OrderModel orderModel = this.convertFromDataObject(orderDO);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModelList;
    }

    public List<OrderModel> getOrderByTitle(Integer userId, Integer page, String title) {
        Integer item_id = this.itemDOMapper.getItemByTitle(title);
        List<OrderDO> orderDOList = this.orderDOMapper.selectByUserId(userId, (page - 1) * 3);
        System.out.println(3);
        //List<OrderDO> orderDOList = this.orderDOMapper.selectByUserIdAndItemId(userId, (page - 1) * 3, item_id);
        List<OrderModel> orderModelList = (List)orderDOList.stream().map((orderDO) -> {
            OrderModel orderModel = this.convertFromDataObject(orderDO);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModelList;
    }

    public Integer getCount(Integer userId) {
        Integer result = this.orderDOMapper.getCountByUserId(userId);
        return result;
    }

    @Transactional
    public LogisticsModel completeOrder(String id, Integer userId, Integer paymentMethod, Integer addressId) {
        OrderDO orderDO = this.orderDOMapper.selectByPrimaryKey(id);
        if (orderDO == null) {
            throw new BusinessException(EmBusinessError.ORDER_NOT_EXIST);
        } else if (orderDO.getUserId() != userId) {
            throw new BusinessException(EmBusinessError.USER_STATUS_ERROR);
        } else {
            ItemModel itemModel = this.itemService.getItemById(orderDO.getItemId());
            orderDO.setPaymentMethod(paymentMethod);
            orderDO.setStatus(2);
            int result = this.orderDOMapper.updateByPrimaryKeySelective(orderDO);
            if (result != 1) {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "更新订单失败");
            } else {
                LogisticsDO logisticsDO = new LogisticsDO();
                logisticsDO.setId(this.generateLogisticsNo());
                logisticsDO.setOrderId(id);
                logisticsDO.setAddressId(addressId);
                logisticsDO.setCreateTime(new Date());
                logisticsDO.setStoreName(itemModel.getStoreName());
                logisticsDO.setStatus(1);
                int result1 = this.logisticsDOMapper.insertSelective(logisticsDO);
                if (result1 != 1) {
                    throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "创建物流失败");
                } else {
                    return this.convertFromDataObject(logisticsDO);
                }
            }
        }
    }

    @Transactional
    public void cancelOrder(String id, Integer userId) {
        OrderDO orderDO = this.orderDOMapper.selectByPrimaryKey(id);
        if (orderDO == null) {
            throw new BusinessException(EmBusinessError.ORDER_NOT_EXIST);
        } else if (orderDO.getUserId() != userId) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN, "登录信息异常");
        } else if (orderDO.getStatus() != 1) {
            throw new BusinessException(EmBusinessError.ORDER_STATUS_ERROR);
        } else {
            this.orderDOMapper.cancelOrder(id);
            this.itemService.increaseStock(orderDO.getItemId(), orderDO.getAmount());
            Boolean result = this.itemService.decreaseSales(orderDO.getItemId(), orderDO.getAmount());
            if (!result) {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "销量异常");
            }
        }
    }

    public List<LogisticsModel> getLogistics(String storeName, Integer page) {
        List<LogisticsDO> logisticsDOList = this.logisticsDOMapper.listByStoreName(storeName, (page - 1) * 3);
        if (logisticsDOList == null) {
            return null;
        } else {
            List<LogisticsModel> logisticsModelList = (List)logisticsDOList.stream().map((logisticsDO) -> {
                LogisticsModel logisticsModel = this.convertFromDataObject(logisticsDO);
                return logisticsModel;
            }).collect(Collectors.toList());
            return logisticsModelList;
        }
    }

    public Integer getCountByStoreName(String storeName) {
        int result = this.logisticsDOMapper.getCountByStoreName(storeName);
        return result;
    }

    public LogisticsModel completeLogistics(String id, String deliveryCompany, String deliveryNumber, String storeName) {
        LogisticsDO logisticsDO = this.logisticsDOMapper.selectByPrimaryKey(id);
        String orderId = this.logisticsDOMapper.getOrderIdById(id);
        if (logisticsDO == null) {
            throw new BusinessException(EmBusinessError.LOGISTICS_NOT_EXIST);
        } else {
            logisticsDO.setDeliveryCompany(deliveryCompany);
            logisticsDO.setDeliveryNumber(deliveryNumber);
            if (!logisticsDO.getStoreName().equals(storeName)) {
                throw new BusinessException(EmBusinessError.LOGISTICS_STATUS_ERROR);
            } else {
                logisticsDO.setStatus(2);
                this.orderDOMapper.changeStatus(orderId);
                this.logisticsDOMapper.updateByPrimaryKeySelective(logisticsDO);
                LogisticsModel logisticsModel = this.convertFromDataObject(logisticsDO);
                return logisticsModel;
            }
        }
    }

    private OrderModel convertFromDataObject(OrderDO orderDO) {
        if (orderDO == null) {
            return null;
        } else {
            OrderModel orderModel = new OrderModel();
            BeanUtils.copyProperties(orderDO, orderModel);
            orderModel.setItemPrice(new BigDecimal(orderDO.getItemPrice()));
            orderModel.setOrderPrice(new BigDecimal(orderDO.getOrderPrice()));
            return orderModel;
        }
    }

    private LogisticsModel convertFromDataObject(LogisticsDO logisticsDO) {
        if (logisticsDO == null) {
            return null;
        } else {
            LogisticsModel logisticsModel = new LogisticsModel();
            BeanUtils.copyProperties(logisticsDO, logisticsModel);
            return logisticsModel;
        }
    }

    private OrderDO convertFromOrderModer(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        } else {
            OrderDO orderDO = new OrderDO();
            BeanUtils.copyProperties(orderModel, orderDO);
            orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
            orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
            return orderDO;
        }
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public String generateOrderNo() {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        SequenceDO sequenceDO = this.sequenceDOMapper.getSequenceByName("order_info");
        int sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        this.sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);

        for(int i = 0; i < 6 - sequenceStr.length(); ++i) {
            stringBuilder.append(0);
        }

        stringBuilder.append(sequenceStr);
        return stringBuilder.toString();
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW
    )
    public String generateLogisticsNo() {
        StringBuilder stringBuilder = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
        stringBuilder.append(nowDate);
        SequenceDO sequenceDO = this.sequenceDOMapper.getSequenceByName("logistics_info");
        int sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        this.sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);

        for(int i = 0; i < 6 - sequenceStr.length(); ++i) {
            stringBuilder.append(0);
        }

        stringBuilder.append(sequenceStr);
        return stringBuilder.toString();
    }
}
