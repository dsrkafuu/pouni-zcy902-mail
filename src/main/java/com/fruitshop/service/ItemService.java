package com.fruitshop.service;

import com.fruitshop.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    ItemModel getItemById(Integer id);

    ItemModel getItemByOrderId(String id);

    String getItemImgUrl(Integer id);

    ItemModel createItem(ItemModel itemModel);

    ItemModel updateItem(ItemModel itemModel);

    List<ItemModel> getItemList(Integer sort, Integer page);

    Integer getCountBySort(Integer sort);

    List<ItemModel> getItemListByUpdateTime(Integer amount);

    List<ItemModel> getItemListByStoreName(String storeName, Integer page);

    Integer getCountByStoreName(String storeName);

    List<ItemModel> searchItem(String keyWord, Integer page);

    Integer getCountByKeyWord(String keyWord);

    Boolean decreaseStock(Integer itemId, Integer amount);

    void increaseStock(Integer itemId, Integer amount);

    void increaseSales(Integer itemId, Integer amount);

    Boolean decreaseSales(Integer itemId, Integer amount);
}
