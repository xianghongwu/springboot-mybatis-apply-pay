package com.xhw.applypay.service.impl;

import com.xhw.applypay.mapper.IProductdao;
import com.xhw.applypay.model.Product;
import com.xhw.applypay.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Classname ProductServiceImpl
 * @Description TODO
 * @Date 2019/3/21 20:48
 * @Created by xhw
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private IProductdao iProductdao;


    @Override
    public List<Product> getProducts() {
        return iProductdao.getAllProduct();
    }

    @Override
    public Product getProductById(String productId) {
        return iProductdao.getProductById(productId);
    }
}
