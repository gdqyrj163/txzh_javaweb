package com.ten.txzh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.RegisterMapper;
import com.ten.txzh.pojo.User;

@Service
public class RegisterService {

	@Autowired
	RegisterMapper registerDao;
	
	public int doReg(User user) {
		int resultCode = 0;
		if(registerDao.RegisterCheck(user) == null) {
			registerDao.doReg(user);
			resultCode = 1; 
		}
		return resultCode;
	}
}
