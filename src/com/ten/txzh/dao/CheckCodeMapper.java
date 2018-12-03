package com.ten.txzh.dao;

import com.ten.txzh.pojo.CheckCode;

public interface CheckCodeMapper {
	int saveCheckCode(CheckCode cc);
	String getCheckCodeTime(String email);
	int deleteCheckCode(String email);
	int checkExist(String email);
	String getCheckCode(String email);
}
