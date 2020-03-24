package com.xhw.applypay.mapper;

import com.xhw.applypay.model.Product;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @Classname IProductdao
 * @Description TODO
 * @Date 2019/3/21 20:50
 * @Created by xhw
 */
public interface IProductdao {


    @Select("select * from product ")
    List<Product> getAllProduct();

    @Select("select * from product where id=#{id}")
    Product getProductById(String id);


}
