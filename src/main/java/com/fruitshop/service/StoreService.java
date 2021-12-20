//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fruitshop.service;

import com.fruitshop.service.model.StoreModel;

public interface StoreService {
    StoreModel getStoreByTelphone(String telphone);

    Boolean isRegisteredByTelphone(String telphone);

    Boolean isRegisterByStoreName(String storeName);

    void register(StoreModel storeModel);

    StoreModel validateLogin(String telphone, String encrptPassword);
}
