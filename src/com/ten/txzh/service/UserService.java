package com.ten.txzh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.UserMapper;
import com.ten.txzh.pojo.User;

@Service
public class UserService {
	
	@Autowired
	UserMapper userDao;
	
	@SuppressWarnings("finally")
	public User getUserInfo(int userid) {
		User user = new User();
		try {
			user = userDao.getUserInfo(userid);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			return user;
			
		}
	}
	
	public String getUserName(int userid) {
		String userName = "";
		userName = userDao.getUserNameByUserid(userid);
		return userName;
	}
	
	public int alterUserInfo(User user) {
		int resultCode = 0;
		if(userDao.alterUserInfo(user) > 0) {
			resultCode = 1;
		}
		return resultCode;
	}
}
