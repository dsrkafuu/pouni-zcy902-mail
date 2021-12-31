
package com.njcci.dao;

import com.njcci.dataobject.OrderDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrderDO record);

    int insertSelective(OrderDO record);

    OrderDO selectByPrimaryKey(String id);

    List<OrderDO> selectByUserId(Integer userId, Integer offset);

    List<OrderDO> selectByUserIdAndItemId(Integer userId, Integer offset, Integer itemId);

    List<OrderDO> selectById(String id, Integer offset);

    List<OrderDO> selectByUserIdAndStatus(Integer userId, Integer offset, Integer status);

    int getCountByUserId(Integer userId);

    int updateByPrimaryKeySelective(OrderDO record);

    int updateByPrimaryKey(OrderDO record);

    int cancelOrder(String id);

    void changeStatus(String id);

    void confirmOrder(String id);

}
