package com.xhw.applypay.mapper;

import com.xhw.applypay.model.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Classname IOrderDao
 * @Description TODO
 * @Date 2019/3/21 20:50
 * @Created by xhw
 */
public interface IOrderDao {


    @Insert("insert into orders (id, order_num, order_status, \n" +
            "      order_amount, paid_amount, product_id, \n" +
            "      buy_counts, create_time, paid_time\n" +
            "      )\n" +
            "    values (#{id,jdbcType=VARCHAR}, #{orderNum,jdbcType=VARCHAR}, #{orderStatus,jdbcType=VARCHAR}, \n" +
            "      #{orderAmount,jdbcType=VARCHAR}, #{paidAmount,jdbcType=VARCHAR}, #{productId,jdbcType=VARCHAR}, \n" +
            "      #{buyCounts,jdbcType=INTEGER}, #{createTime,jdbcType=TIMESTAMP}, #{paidTime,jdbcType=TIMESTAMP}\n" +
            "      )")
    void insert(Orders order);


    Orders getOrderById(@Param("orderNum") String orderNum);


    int updateById(Orders orders);

    @Select("select * from orders")
    List<Orders> getOrderList();
}
