//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.service.impl;

import com.njcci.dao.ItemDOMapper;
import com.njcci.dao.ItemStockDOMapper;
import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.service.ItemService;
import com.njcci.service.OrderService;
import com.njcci.dataobject.ItemDO;
import com.njcci.dataobject.ItemStockDO;
import com.njcci.service.model.ItemModel;
import com.njcci.service.model.OrderModel;
import com.njcci.validator.ValidationResult;
import com.njcci.validator.ValidatorImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;
    @Autowired
    private OrderService orderService;

    public ItemServiceImpl() {
    }

    public ItemModel getItemById(Integer id) {
        ItemDO itemDo = this.itemDOMapper.selectByPrimaryKey(id);
        if (itemDo == null) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        } else if (itemDo.getStatus() == 2) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_SALE);
        } else {
            ItemStockDO itemStockDO = this.itemStockDOMapper.selectByItemId(id);
            if (itemStockDO == null) {
                return null;
            } else {
                ItemModel itemModel = this.convertFromDataObject(itemDo, itemStockDO);
                return itemModel;
            }
        }
    }

    public ItemModel getItemByOrderId(String id) {
        OrderModel orderModel = this.orderService.getOrder(id);
        if (orderModel == null) {
            return null;
        } else {
            ItemModel itemModel = this.getItemById(orderModel.getItemId());
            return itemModel;
        }
    }

    public String getItemImgUrl(Integer id) {
        String imgUrl = this.itemDOMapper.getItemImgUrlById(id);
        if (imgUrl == null) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        } else {
            return imgUrl;
        }
    }

    @Transactional
    public ItemModel createItem(ItemModel itemModel) {
        ValidationResult validationResult = this.validator.validate(itemModel);
        if (validationResult.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
        } else {
            ItemDO itemDO = this.convertItemDOFromModel(itemModel);
            int result = this.itemDOMapper.insertSelective(itemDO);
            if (result != 1) {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "????????????");
            } else {
                itemModel.setId(itemDO.getId());
                ItemStockDO itemStockDO = this.convertItemStockDOFromModel(itemModel);
                int result1 = this.itemStockDOMapper.insertSelective(itemStockDO);
                if (result1 != 1) {
                    throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "????????????");
                } else {
                    return itemModel;
                }
            }
        }
    }

    @Transactional
    public ItemModel updateItem(ItemModel itemModel) {
        ValidationResult validationResult = this.validator.validate(itemModel);
        if (validationResult.isHasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, validationResult.getErrMsg());
        } else {
            ItemDO itemDO = this.convertItemDOFromModel(itemModel);
            int result = this.itemDOMapper.updateByPrimaryKeySelective(itemDO);
            if (result != 1) {
                throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "????????????");
            } else {
                ItemStockDO itemStockDO = this.convertItemStockDOFromModel(itemModel);
                int result1 = this.itemStockDOMapper.updateByItemIdSelective(itemStockDO);
                if (result1 != 1) {
                    throw new BusinessException(EmBusinessError.UNKNOWN_ERROR, "????????????");
                } else {
                    return this.getItemById(itemModel.getId());
                }
            }
        }
    }

    public List<ItemModel> getItemList(Integer sort, Integer page) {
        List<ItemDO> itemDOList = this.itemDOMapper.selectBySort(sort, (page - 1) * 10);
        if (itemDOList != null && itemDOList.size() != 0) {
            List<ItemModel> itemModelList = this.convertFromDOList(itemDOList);
            return itemModelList;
        } else {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST, "???????????????????????????");
        }
    }

    public Integer getCountBySort(Integer sort) {
        Integer count = this.itemDOMapper.getCountBySort(sort);
        return count;
    }

    public List<ItemModel> getItemListByUpdateTime(Integer amount) {
        List<ItemDO> itemDOList = this.itemDOMapper.selectByUpdateTime(amount);
        List<ItemModel> itemModelList = this.convertFromDOList(itemDOList);
        return itemModelList;
    }

    public List<ItemModel> getItemListByStoreName(String storeName, Integer page) {
        List<ItemDO> itemDOList = this.itemDOMapper.selectByStoreName(storeName, (page - 1) * 10);
        List<ItemModel> itemModelList = this.convertFromDOList(itemDOList);
        return itemModelList;
    }

    public Integer getCountByStoreName(String storeName) {
        Integer count = this.itemDOMapper.getCountByStoreName(storeName);
        return count;
    }

    public List<ItemModel> searchItem(String keyWord, Integer page) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('%');
        stringBuilder.append(keyWord);
        stringBuilder.append('%');
        List<ItemDO> itemDOList = this.itemDOMapper.selectByKeyWord(stringBuilder.toString(), (page - 1) * 10);
        if (itemDOList != null && itemDOList.size() != 0) {
            List<ItemModel> itemModelList = this.convertFromDOList(itemDOList);
            return itemModelList;
        } else {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST, "?????????????????????");
        }
    }

    public Integer getCountByKeyWord(String keyWord) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('%');
        stringBuilder.append(keyWord);
        stringBuilder.append('%');
        Integer count = this.itemDOMapper.getCountByKeyWord(stringBuilder.toString());
        if (count == 0) {
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST, "?????????????????????");
        } else {
            return count;
        }
    }

    public Boolean decreaseStock(Integer itemId, Integer amount) {
        int result = this.itemStockDOMapper.decreaseStock(itemId, amount);
        return result == 1 ? true : false;
    }

    public void increaseStock(Integer itemId, Integer amount) {
        this.itemStockDOMapper.increaseStock(itemId, amount);
    }

    public void increaseSales(Integer itemId, Integer amount) {
        this.itemDOMapper.increaseSales(itemId, amount);
    }

    public Boolean decreaseSales(Integer itemId, Integer amount) {
        int result = this.itemDOMapper.decreaseSales(itemId, amount);
        return result == 1 ? true : false;
    }

    private ItemModel convertFromDataObject(ItemDO itemDO, ItemStockDO itemStockDO) {
        if (itemDO != null && itemStockDO != null) {
            ItemModel itemModel = new ItemModel();
            BeanUtils.copyProperties(itemDO, itemModel);
            itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
            itemModel.setStock(itemStockDO.getStock());
            return itemModel;
        } else {
            return null;
        }
    }

    private List<ItemModel> convertFromDOList(List<ItemDO> itemDOList) {
        List<ItemModel> itemModelList = (List)itemDOList.stream().map((itemDO) -> {
            ItemStockDO itemStockDO = this.itemStockDOMapper.selectByItemId(itemDO.getId());
            if (itemStockDO == null) {
                return null;
            } else {
                ItemModel itemModel = this.convertFromDataObject(itemDO, itemStockDO);
                return itemModel;
            }
        }).collect(Collectors.toList());
        return itemModelList;
    }

    private ItemDO convertItemDOFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        } else {
            ItemDO itemDO = new ItemDO();
            BeanUtils.copyProperties(itemModel, itemDO);
            itemDO.setPrice(itemModel.getPrice().doubleValue());
            return itemDO;
        }
    }

    private ItemStockDO convertItemStockDOFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        } else {
            ItemStockDO itemStockDO = new ItemStockDO();
            itemStockDO.setItemId(itemModel.getId());
            itemStockDO.setStock(itemModel.getStock());
            return itemStockDO;
        }
    }
}
