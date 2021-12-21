
package com.njcci.dao;

import com.njcci.dataobject.StoreDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreDO record);

    int insertSelective(StoreDO record);

    StoreDO selectByPrimaryKey(Integer id);

    StoreDO selectByTelphone(String telphone);

    StoreDO selectByStoreName(String storeName);

    int updateByPrimaryKeySelective(StoreDO record);

    int updateByPrimaryKey(StoreDO record);
}
