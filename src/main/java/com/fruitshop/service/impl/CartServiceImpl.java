//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fruitshop.service.impl;

import com.fruitshop.dao.CartDOMapper;
import com.fruitshop.error.BusinessException;
import com.fruitshop.error.EmBusinessError;
import com.fruitshop.service.CartService;
import com.fruitshop.service.ItemService;
import com.fruitshop.dataobject.CartDO;
import com.fruitshop.service.model.CartModel;
import com.fruitshop.service.model.ItemModel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartDOMapper cartDOMapper;

    public CartServiceImpl() {
    }

    @Transactional
    public void addToCart(Integer itemId, Integer userId, Integer amount) {
        ItemModel itemModel = this.itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        } else if (itemModel.getStatus() == 2) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_SALE);
        } else {
            CartDO cartDO = this.cartDOMapper.selectByItemIdAndUserId(itemId, userId);
            CartModel cartModel;
            CartDO cartDO1;
            if (cartDO == null) {
                cartModel = new CartModel();
                cartModel.setItemId(itemId);
                cartModel.setUserId(userId);
                cartModel.setAmount(amount);
                cartModel.setPrice(itemModel.getPrice());
                cartModel.setTotalPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
                cartModel.setStatus(1);
                cartModel.setCreateTime(new Date());
                cartModel.setUpdateTime(new Date());
                cartDO1 = this.convertFromModel(cartModel);
                this.cartDOMapper.insertSelective(cartDO1);
            } else if (cartDO.getUpdateTime().before(itemModel.getUpdateTime())) {
                this.cartDOMapper.invalidCartContent(cartDO.getId());
                cartModel = new CartModel();
                cartModel.setItemId(itemId);
                cartModel.setUserId(userId);
                cartModel.setAmount(amount);
                cartModel.setPrice(itemModel.getPrice());
                cartModel.setTotalPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
                cartModel.setStatus(1);
                cartModel.setCreateTime(new Date());
                cartModel.setUpdateTime(new Date());
                cartDO1 = this.convertFromModel(cartModel);
                this.cartDOMapper.insertSelective(cartDO1);
            } else {
                Integer newAmount = cartDO.getAmount() + amount;
                cartDO.setAmount(newAmount);
                cartDO.setTotalPrice(itemModel.getPrice().multiply(new BigDecimal(newAmount)).doubleValue());
                cartDO.setUpdateTime(new Date());
                this.cartDOMapper.updateByPrimaryKeySelective(cartDO);
            }

        }
    }

    @Transactional
    public void deleteFromCart(Integer id, Integer userId) {
        CartDO cartDO = this.cartDOMapper.selectByPrimaryKey(id);
        if (cartDO == null) {
            throw new BusinessException(EmBusinessError.CART_NOT_EXIST);
        } else if (cartDO.getUserId() != userId) {
            throw new BusinessException(EmBusinessError.USER_STATUS_ERROR);
        } else {
            int result = this.cartDOMapper.deleteCartContent(id);
            if (result != 1) {
                throw new BusinessException(EmBusinessError.CART_STATUS_ERROR);
            }
        }
    }

    @Transactional
    public CartModel updateCart(Integer id, Integer amount, Integer userId) {
        CartDO cartDO = this.cartDOMapper.selectByPrimaryKey(id);
        if (cartDO == null) {
            throw new BusinessException(EmBusinessError.CART_NOT_EXIST);
        } else if (cartDO.getUserId() != userId) {
            throw new BusinessException(EmBusinessError.USER_STATUS_ERROR);
        } else if (cartDO.getStatus() != 1) {
            throw new BusinessException(EmBusinessError.CART_STATUS_ERROR);
        } else {
            ItemModel itemModel = this.itemService.getItemById(cartDO.getItemId());
            CartModel cartModel;
            if (cartDO.getUpdateTime().before(itemModel.getUpdateTime())) {
                this.cartDOMapper.invalidCartContent(cartDO.getId());
                cartDO.setStatus(3);
                cartModel = this.convertFromDO(cartDO);
                return cartModel;
            } else {
                cartDO.setAmount(amount);
                cartDO.setTotalPrice(cartDO.getPrice() * (double)amount);
                cartDO.setUpdateTime(new Date());
                this.cartDOMapper.updateByPrimaryKeySelective(cartDO);
                cartModel = this.convertFromDO(cartDO);
                return cartModel;
            }
        }
    }

    @Transactional
    public List<CartModel> list(Integer userId) {
        List<CartDO> cartDOList = this.cartDOMapper.selectByUserId(userId);
        if (cartDOList != null && cartDOList.size() != 0) {
            List<CartModel> cartModelList = (List)cartDOList.stream().map((cartDO) -> {
                if (cartDO.getStatus() == 1) {
                    ItemModel itemModel = this.itemService.getItemById(cartDO.getItemId());
                    if (cartDO.getUpdateTime().before(itemModel.getUpdateTime())) {
                        this.cartDOMapper.invalidCartContent(cartDO.getId());
                        cartDO.setStatus(3);
                    }
                }

                CartModel cartModel = this.convertFromDO(cartDO);
                return cartModel;
            }).collect(Collectors.toList());
            return cartModelList;
        } else {
            return null;
        }
    }

    public CartModel getCart(Integer id) {
        CartDO cartDO = this.cartDOMapper.selectByPrimaryKey(id);
        if (cartDO == null) {
            throw new BusinessException(EmBusinessError.CART_NOT_EXIST);
        } else if (cartDO.getStatus() != 1) {
            throw new BusinessException(EmBusinessError.CART_STATUS_ERROR);
        } else {
            CartModel cartModel = this.convertFromDO(cartDO);
            return cartModel;
        }
    }

    private CartDO convertFromModel(CartModel cartModel) {
        if (cartModel == null) {
            return null;
        } else {
            CartDO cartDO = new CartDO();
            BeanUtils.copyProperties(cartModel, cartDO);
            cartDO.setPrice(cartModel.getPrice().doubleValue());
            cartDO.setTotalPrice(cartModel.getTotalPrice().doubleValue());
            return cartDO;
        }
    }

    private CartModel convertFromDO(CartDO cartDO) {
        if (cartDO == null) {
            return null;
        } else {
            CartModel cartModel = new CartModel();
            BeanUtils.copyProperties(cartDO, cartModel);
            cartModel.setPrice(new BigDecimal(cartDO.getPrice()));
            cartModel.setTotalPrice(new BigDecimal(cartDO.getTotalPrice()));
            return cartModel;
        }
    }
}
