//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.dao;

import com.njcci.dataobject.SequenceDO;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceDOMapper {
    int insert(SequenceDO record);

    int insertSelective(SequenceDO record);

    SequenceDO getSequenceByName(String name);

    int updateByPrimaryKeySelective(SequenceDO record);
}
