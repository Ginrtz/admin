package com.gin.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void test() {
		String admin = redisTemplate.boundValueOps("admin").get();
		System.out.println(admin);
	}
}
