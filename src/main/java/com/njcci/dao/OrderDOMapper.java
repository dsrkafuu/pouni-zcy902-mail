
package com.njcci.dao;

import com.njcci.dataobject.OrderDO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderDO record);

    int insertSelective(OrderDO record);

    OrderDO selectByPrimaryKey(String id);

    List<OrderDO> selectByUserId(Integer userId, Integer offset);

    int getCountByUserId(Integer userId);

    int updateByPrimaryKeySelective(OrderDO record);

    int updateByPrimaryKey(OrderDO record);

    int cancelOrder(String id);
}
