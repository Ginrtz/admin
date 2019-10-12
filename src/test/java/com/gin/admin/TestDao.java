package com.gin.admin;

import com.gin.nicedao.annotation.NiceDao;

@NiceDao
public interface TestDao {
	void test(int a, String b);
}
