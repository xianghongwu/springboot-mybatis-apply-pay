package com.xhw.applypay.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class User implements Serializable {
    private String id;

    private String username;

    private String sex;

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", sex=" + sex + "]";
	}
    
}