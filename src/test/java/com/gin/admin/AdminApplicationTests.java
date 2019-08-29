package com.gin.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gin.admin.jdbc.dao.IDAO;
import com.gin.admin.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {
	@Autowired
	private IDAO dao;

	@Test
	public void test() {
		User user = dao.findFirst(User.class, "select * from user");
		System.out.println(user);
	}

	@Test
	public void test2() {
	}
}
