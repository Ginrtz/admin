package com.gin.admin;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gin.admin.cg.util.CgUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {

	@Autowired
	private CgUtil util;

	@Test
	public void test() {
		try {
			util.genModel("com.gin.admin.model", "user");
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}
}
