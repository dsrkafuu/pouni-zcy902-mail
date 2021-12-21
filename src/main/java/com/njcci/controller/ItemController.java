package com.njcci.controller;

import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import com.njcci.response.CommonReturnType;
import com.njcci.service.ItemService;
import com.njcci.service.model.ItemModel;
import com.njcci.service.model.StoreModel;
import com.njcci.controller.viewobject.ItemVO;
import com.njcci.util.FileUploadUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/item"})
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true"
)
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    private final ResourceLoader resourceLoader;
    @Value("${web.upload-path}")
    private String path;

    @Autowired
    public ItemController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping(
            value = {"/get"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel = this.itemService.getItemById(id);
        ItemVO itemVO = this.convertFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    @RequestMapping(
            value = {"/getImgUrl"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getItemImgUrl(@RequestParam(name = "id") Integer id) {
        String imgUrl = this.itemService.getItemImgUrl(id);
        return CommonReturnType.create(imgUrl);
    }

    @RequestMapping(
            value = {"/create"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType createItem(@RequestParam(name = "title") String title, @RequestParam(name = "description") String description, @RequestParam(name = "sort") Integer sort, @RequestParam(name = "imgUrl") String imgUrl, @RequestParam(name = "price") BigDecimal price, @RequestParam(name = "stock") Integer stock) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            ItemModel itemModel = new ItemModel();
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            itemModel.setStoreName(storeModel.getStoreName());
            itemModel.setTitle(title);
            itemModel.setDescription(description);
            itemModel.setSort(sort);
            itemModel.setSales(0);
            itemModel.setImgUrl(imgUrl);
            itemModel.setPrice(price);
            itemModel.setStock(stock);
            itemModel.setStatus(1);
            itemModel.setCreateTime(new Date());
            itemModel.setUpdateTime(new Date());
            ItemModel returnItemModel = this.itemService.createItem(itemModel);
            ItemVO itemVO = this.convertFromModel(returnItemModel);
            return CommonReturnType.create(itemVO);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
        }
    }

    @RequestMapping(
            value = {"/update"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType updateItem(@RequestParam(name = "id") Integer id, @RequestParam(name = "title") String title, @RequestParam(name = "description") String description, @RequestParam(name = "sort") Integer sort, @RequestParam(name = "imgUrl") String imgUrl, @RequestParam(name = "price") BigDecimal price, @RequestParam(name = "stock") Integer stock) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            ItemModel itemModel = new ItemModel();
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            itemModel.setStoreName(storeModel.getStoreName());
            itemModel.setId(id);
            itemModel.setTitle(title);
            itemModel.setDescription(description);
            itemModel.setSort(sort);
            itemModel.setImgUrl(imgUrl);
            itemModel.setPrice(price);
            itemModel.setStock(stock);
            itemModel.setStatus(1);
            itemModel.setUpdateTime(new Date());
            ItemModel returnItemModel = this.itemService.updateItem(itemModel);
            ItemVO itemVO = this.convertFromModel(returnItemModel);
            return CommonReturnType.create(itemVO);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
        }
    }

    @RequestMapping(
            value = {"/list"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getItemList(@RequestParam(name = "sort") Integer sort, @RequestParam(name = "page") Integer page) {
        List<ItemModel> itemModelList = this.itemService.getItemList(sort, page);
        List<ItemVO> itemVOList = this.convertFromModelList(itemModelList);
        return CommonReturnType.create(itemVOList);
    }

    @RequestMapping(
            value = {"/count"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getItemCount(@RequestParam(name = "sort") Integer sort) {
        Integer count = this.itemService.getCountBySort(sort);
        return CommonReturnType.create(count);
    }

    @RequestMapping(
            value = {"/listbyupdatetime"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getItemListByUpdateTime() {
        List<ItemModel> itemModelList = this.itemService.getItemListByUpdateTime(2);
        List<ItemVO> itemVOList = this.convertFromModelList(itemModelList);
        return CommonReturnType.create(itemVOList);
    }

    @RequestMapping(
            value = {"/listbystorename"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getItemListByStoreName(@RequestParam(name = "storeName") String storeName, @RequestParam(name = "page") Integer page) {
        List<ItemModel> itemModelList = this.itemService.getItemListByStoreName(storeName, page);
        List<ItemVO> itemVOList = this.convertFromModelList(itemModelList);
        return CommonReturnType.create(itemVOList);
    }

    @RequestMapping(
            value = {"/storeitemlist"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getStoreItemList(@RequestParam(name = "page") Integer page) {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            return this.getItemListByStoreName(storeModel.getStoreName(), page);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
        }
    }

    @RequestMapping(
            value = {"/storeitemscount"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getCountByStoreName() {
        Boolean isStoreLogin = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if (isStoreLogin != null && isStoreLogin) {
            StoreModel storeModel = (StoreModel)this.httpServletRequest.getSession().getAttribute("LOGIN_STORE");
            Integer count = this.itemService.getCountByStoreName(storeModel.getStoreName());
            return CommonReturnType.create(count);
        } else {
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN, "商铺登录信息失效");
        }
    }

    @RequestMapping(
            value = {"/search"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType searchByKeyWord(@RequestParam(name = "keyWord") String keyWord, @RequestParam(name = "page") Integer page) {
        List<ItemModel> itemModelList = this.itemService.searchItem(keyWord, page);
        List<ItemVO> itemVOList = this.convertFromModelList(itemModelList);
        return CommonReturnType.create(itemVOList);
    }

    @RequestMapping(
            value = {"/searchresult"},
            method = {RequestMethod.POST},
            consumes = {"application/x-www-form-urlencoded"}
    )
    public CommonReturnType getCountByKeyWord(@RequestParam(name = "keyWord") String keyWord) {
        Integer count = this.itemService.getCountByKeyWord(keyWord);
        return CommonReturnType.create(count);
    }

    @RequestMapping(
            value = {"/getitembyorder"},
            method = {RequestMethod.GET}
    )
    public CommonReturnType getItemByOrderId(@RequestParam(name = "id") String id) {
        ItemModel itemModel = this.itemService.getItemByOrderId(id);
        ItemVO itemVO = this.convertFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    @RequestMapping({"/single"})
    public CommonReturnType singleImage(@RequestParam(name = "file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String url_path = FileUploadUtil.uploadImage(file, this.path, fileName);
        return CommonReturnType.create(url_path);
    }

    private ItemVO convertFromModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        } else {
            ItemVO itemVO = new ItemVO();
            BeanUtils.copyProperties(itemModel, itemVO);
            return itemVO;
        }
    }

    private List<ItemVO> convertFromModelList(List<ItemModel> itemModelList) {
        if (itemModelList != null && itemModelList.size() != 0) {
            List<ItemVO> itemVOList = (List)itemModelList.stream().map((itemModel) -> {
                ItemVO itemVO = this.convertFromModel(itemModel);
                return itemVO;
            }).collect(Collectors.toList());
            return itemVOList;
        } else {
            return null;
        }
    }
}
