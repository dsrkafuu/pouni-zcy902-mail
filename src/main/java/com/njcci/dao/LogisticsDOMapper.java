
package com.njcci.dao;

import com.njcci.dataobject.LogisticsDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(LogisticsDO record);

    int insertSelective(LogisticsDO record);

    LogisticsDO selectByPrimaryKey(String id);

    List<LogisticsDO> listByStoreName(String storeName, Integer offset);

    List<LogisticsDO> listByStoreNameAndOrderId(String storeName, Integer offset, String orderId);

    List<LogisticsDO> listByStoreNameAndAddressId(String storeName, Integer offset, Integer addressId);

    List<LogisticsDO> listByStoreNameAndDeliveryName(String storeName, Integer offset, String deliveryName);

    int getCountByStoreName(String storeName);

    String getOrderIdById(String id);

    String getCompanyByOrderId(String orderId);

    String getNumberByOrderId(String orderId);

    int updateByPrimaryKeySelective(LogisticsDO record);

    int updateByPrimaryKey(LogisticsDO record);
}
