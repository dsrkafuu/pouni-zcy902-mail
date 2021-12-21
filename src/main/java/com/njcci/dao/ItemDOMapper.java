
package com.njcci.dao;

import com.njcci.dataobject.ItemDO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    List<ItemDO> selectByStatus(Integer status);

    String getItemImgUrlById(Integer id);

    List<ItemDO> selectBySort(Integer sort, Integer offset);

    Integer getCountBySort(Integer sort);

    List<ItemDO> selectByUpdateTime(Integer amount);

    List<ItemDO> selectByKeyWord(String keyWord, Integer offset);

    Integer getCountByKeyWord(String keyWord);

    List<ItemDO> selectByStoreName(String storeName, Integer offset);

    Integer getCountByStoreName(String storeName);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);

    int increaseSales(Integer id, Integer amount);

    int decreaseSales(Integer id, Integer amount);
}
