package com.ten.txzh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.LoginMapper;
import com.ten.txzh.pojo.User;

@Service
public class LoginService {

	@Autowired
	LoginMapper loginDao;
	
	public User LoginCheck(User user) {
		User resultUser = null;
		resultUser = loginDao.LoginCheck(user);
		return resultUser;
	}
	
}
