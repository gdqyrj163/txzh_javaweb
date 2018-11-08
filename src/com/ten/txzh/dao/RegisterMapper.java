package com.ten.txzh.dao;

import com.ten.txzh.pojo.User;

public interface RegisterMapper {
	User RegisterCheck(User user);
	int doReg(User user);
}

