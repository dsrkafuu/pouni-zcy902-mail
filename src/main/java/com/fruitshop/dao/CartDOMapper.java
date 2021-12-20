package com.fruitshop.dao;

import com.fruitshop.dataobject.CartDO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartDO record);

    int insertSelective(CartDO record);

    CartDO selectByPrimaryKey(Integer id);

    CartDO selectByItemIdAndUserId(Integer itemId, Integer userId);

    List<CartDO> selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(CartDO record);

    int updateByPrimaryKey(CartDO record);

    int deleteCartContent(Integer id);

    int invalidCartContent(Integer id);

    int updateAmount(CartDO record);
}
