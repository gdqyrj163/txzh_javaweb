package com.ten.txzh.dao;

import com.ten.txzh.pojo.User;

public interface UserMapper {
	User getUserInfo(int userid);
	String getUserNameByUserid(int userid);
}
