package com.xhw.applypay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.xhw.applypay.mapper")
public class SpringbootMybatisApplyPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMybatisApplyPayApplication.class, args);
	}

}
