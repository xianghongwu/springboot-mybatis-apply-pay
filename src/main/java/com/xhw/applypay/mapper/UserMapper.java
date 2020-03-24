package com.xhw.applypay.mapper;

import com.xhw.applypay.model.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from db_user")
    List<User> getUserlist();
}
