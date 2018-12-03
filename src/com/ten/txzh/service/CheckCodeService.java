package com.ten.txzh.service;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.CheckCodeMapper;
import com.ten.txzh.pojo.CheckCode;

@Service
public class CheckCodeService {
	@Autowired
	CheckCodeMapper ccDao;
	
	public int saveCheckCode(CheckCode cc) {
		int updateRow = 0;
		updateRow = ccDao.saveCheckCode(cc);
		return updateRow;
	}
	
	public int deleteCheckCode(String email) {
		int updateRow = 0;
		updateRow = ccDao.deleteCheckCode(email);
		return updateRow;
	}
	
	public boolean checkTime(String email, String lastTime) {
		String saveTime = "";
		saveTime = ccDao.getCheckCodeTime(email);
		BigInteger lastTimeInt = new BigInteger(lastTime);
		BigInteger saveTimeInt = new BigInteger(saveTime);
		BigInteger passTime = lastTimeInt.subtract(saveTimeInt);
		if(passTime.intValue() > 200) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean checkExist(String email) {
		int resultRow = 0;
		resultRow = ccDao.checkExist(email);
		if(resultRow > 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean compareCheckCode(String email, String inputCheckCode) {
		String saveCheckCode = "";
		saveCheckCode = ccDao.getCheckCode(email);
		if(saveCheckCode.equals(inputCheckCode)) {
			return true;
		}else {
			return false;
		}
	}
}
