package com.gin.admin;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gin.admin.model.Menu;
import com.gin.admin.model.User;
import com.gin.nicedao.NiceDao;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {
	@Autowired
	private NiceDao dao;
	@Autowired
	private TestDao testDao;

	@Test
	public void test() {
		testDao.test(1, "2");
	}

	@Test
	public void test2() {
		User user = new User();
		user.setUserName("tom");
		user.setPassword("111111");
		user.setEmail("tommy@admin.com");
		user.setMobile("13712345678");
		user.setNickName("tommy");
		user.setQqNo("123456");
		user.setWxNo("wx_123456");
		System.out.println(user);
		dao.save(user);
		System.out.println(user);
	}

	@Test
	public void test3() {
		User user = new User();
		user.setId(6);
		user.setUserName("jerry");
		user.setPassword("123456");
		user.setQqNo("111111");
		dao.update(user, true);
	}

	@Test
	public void test4() {
		User user = new User();
		user.setPassword("111111");
		List<User> list = dao.getList(user);
		System.out.println(list);
	}

	@Test
	public void test5() {
		dao.delete(User.class, 3);
	}

	@Test
	public void test6() {
		User user = dao.getEntity(User.class, 1);
		System.out.println(user);
	}

	@Test
	public void test7() {
		Menu menu = dao.getEntity(Menu.class, 2);
		System.out.println(menu);
	}
}
