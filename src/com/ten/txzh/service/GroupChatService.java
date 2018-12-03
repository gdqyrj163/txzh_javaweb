package com.ten.txzh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.GroupChatMapper;
import com.ten.txzh.pojo.GroupChat;

@Service
public class GroupChatService {
	@Autowired
	GroupChatMapper gcDao;
	
	@SuppressWarnings("finally")
	public List<GroupChat> getUserHistoryGroupChat(int userid){
		List<GroupChat> gcList = new ArrayList<GroupChat>();
		try {
			gcList = gcDao.getUserHistoryGroupChat(userid);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			return gcList;
		}
	}
	
	public int setUserGroupChatLastTime(GroupChat gc) {
		int resultCode = 0;
		if(gcDao.checkUGCTime(gc) > 0) {
			System.out.println("Change time.");
			if(gcDao.updateUserGroupChatLastTime(gc) > 0) {
				resultCode = 1;
			}
		}else {
			System.out.println("New set time.");
			if(gcDao.setUserGroupChatLastTime(gc) > 0) {
				resultCode = 1;
			}
		}
		return resultCode;
	}
	
	@SuppressWarnings("finally")
	public String getUserGroupChatLastTime(GroupChat gc) {
		String lastTime = "";
		try {
			lastTime = gcDao.getUserGroupChatLastTime(gc);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			return lastTime;
		}
	}
	
	public int checkGroupChatExist(GroupChat gc) {
		int resultNum = 0;
		resultNum = gcDao.checkGroupChatExist(gc);
		return resultNum;
	}
	
	public int removeGroupChat(GroupChat gc) {
		int updateRow = 0;
		updateRow = gcDao.removeGroupChat(gc);
		return updateRow;
	}
}
