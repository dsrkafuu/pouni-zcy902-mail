package com.njcci.dao;

import com.njcci.dataobject.AddressDO;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    int selectIdByAddressName(String addressName);
}