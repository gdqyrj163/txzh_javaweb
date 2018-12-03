package com.ten.txzh.dao;

import java.util.List;

import com.ten.txzh.pojo.GroupChat;

public interface GroupChatMapper {
	int checkGroupChatExist(GroupChat gc);
	int setUserGroupChatLastTime(GroupChat gc);
	int updateUserGroupChatLastTime(GroupChat gc);
	int checkUGCTime(GroupChat gc);
	String getUserGroupChatLastTime(GroupChat gc);
	List<GroupChat> getUserHistoryGroupChat(int userid);
	int removeGroupChat(GroupChat gc);
}
