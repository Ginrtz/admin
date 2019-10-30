package com.gin.admin.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gin.admin.dao.BaseDao;
import com.gin.admin.model.Menu;
import com.gin.admin.model.base.ResResult;
import com.gin.admin.util.TreeBeanUtil;

@RestController
@RequestMapping("/menu")
public class MenuApi {
	@Autowired
	private BaseDao dao;

	@RequestMapping("/list")
	public ResResult list() {
		List<Menu> menuList = dao.findList(Menu.class, "select * from menu");
		List<Menu> treeMenu = TreeBeanUtil.listToTreeList(menuList, null);
		Map<String, Object> data = new HashMap<>();
		data.put("items", treeMenu);
		ResResult result = new ResResult();
		result.setData(data);
		return result;
	}
}
