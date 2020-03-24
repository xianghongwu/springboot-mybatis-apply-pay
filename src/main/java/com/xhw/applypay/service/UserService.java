package com.xhw.applypay.service;


import com.xhw.applypay.model.User;

import java.util.List;

/**
 * @Classname AlipayController
 * @Description TODO
 * @Date 2019/3/21 20:40
 * @Created by xhw
 */
public interface UserService {

	/**
	 * 
	 * @Description: 新增用户
	 */
	public void saveUser(User user);
	
	/**
	 * 
	 * @Description: 更新用户
	 */
	public void updateUserById(User user);
	
	/**
	 * 
	 * @Description: 删除用户
	 */
	public void deleteUserById(String userId);
	
	/**
	 * 
	 * @Description: 根据用户主键ID获取用户信息
	 */
	public User getUserById(String userId);
	
	/**
	 * 
	 * @Description: 获取用户列表
	 */
	public List<User> getUserList();
}
