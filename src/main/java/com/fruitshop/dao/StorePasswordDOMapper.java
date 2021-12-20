
package com.fruitshop.dao;

import com.fruitshop.dataobject.StorePasswordDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StorePasswordDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StorePasswordDO record);

    int insertSelective(StorePasswordDO record);

    StorePasswordDO selectByPrimaryKey(Integer id);

    StorePasswordDO selectByStoreId(Integer storeId);

    int updateByPrimaryKeySelective(StorePasswordDO record);

    int updateByPrimaryKey(StorePasswordDO record);
}
