
package com.fruitshop.dao;

import com.fruitshop.dataobject.ItemStockDO;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStockDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemStockDO record);

    int insertSelective(ItemStockDO record);

    ItemStockDO selectByPrimaryKey(Integer id);

    ItemStockDO selectByItemId(Integer itemId);

    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByItemIdSelective(ItemStockDO record);

    int updateByPrimaryKey(ItemStockDO record);

    int decreaseStock(Integer itemId, Integer amount);

    int increaseStock(Integer itemId, Integer amount);
}
