package com.gin.admin;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.gin.admin.dao.BaseDao;
import com.gin.admin.model.Menu;
import com.gin.admin.util.TreeBeanUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {

	@Autowired
	private BaseDao dao;

	@Test
	public void test() {
		List<Menu> menuList = dao.findList(Menu.class, "select * from menu");
		List<Menu> treeMenu = TreeBeanUtil.listToTreeList(menuList, null);
		System.out.println(JSON.toJSONString(treeMenu, true));
	}
}
