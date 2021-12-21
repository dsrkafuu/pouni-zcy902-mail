
package com.njcci.service;

import com.njcci.service.model.AddressModel;
import java.util.List;

public interface AddressService {
    AddressModel get(Integer id);

    AddressModel create(AddressModel addressModel);

    List<AddressModel> list(Integer userId);

    void setAddress(Integer id, Integer userId);
}
