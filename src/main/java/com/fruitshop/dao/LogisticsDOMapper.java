
package com.fruitshop.dao;

import com.fruitshop.dataobject.LogisticsDO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(LogisticsDO record);

    int insertSelective(LogisticsDO record);

    LogisticsDO selectByPrimaryKey(String id);

    List<LogisticsDO> listByStoreName(String storeName, Integer offset);

    int getCountByStoreName(String storeName);

    int updateByPrimaryKeySelective(LogisticsDO record);

    int updateByPrimaryKey(LogisticsDO record);
}
