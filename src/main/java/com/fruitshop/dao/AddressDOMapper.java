package com.fruitshop.dao;

import com.fruitshop.dataobject.AddressDO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AddressDO record);

    int insertSelective(AddressDO record);

    AddressDO selectByPrimaryKey(Integer id);

    List<AddressDO> selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(AddressDO record);

    int updateByPrimaryKey(AddressDO record);

    int setMainAddress(Integer id);

    int setUsualAddress(Integer userId);
}