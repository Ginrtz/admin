package com.gin.admin.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gin.admin.model.Menu;
import com.gin.admin.model.base.ResponseResult;
import com.gin.admin.util.TreeBeanUtil;
import com.gin.nicedao.NiceDao;

@RestController
@RequestMapping("/menu")
public class MenuApi {
	@Autowired
	private NiceDao dao;

	@RequestMapping("/list")
	public ResponseResult list() {
		List<Menu> menuList = dao.getList(new Menu());
		List<Menu> treeMenu = TreeBeanUtil.listToTreeList(menuList, null);
		Map<String, Object> data = new HashMap<>();
		data.put("items", treeMenu);
		ResponseResult result = new ResponseResult();
		result.setData(data);
		return result;
	}
}
