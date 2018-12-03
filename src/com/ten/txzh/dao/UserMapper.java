package com.ten.txzh.dao;

import com.ten.txzh.pojo.User;

public interface UserMapper {
	User getUserInfo(int userid);
	String getUserNameByUserid(int userid);
	int alterUserInfo(User user);
	int changePassword(User user);
	int getUseridByEmail(String email);
}
