package com.ten.txzh.dao;

import com.ten.txzh.pojo.GroupChat;

public interface GroupChatMapper {
	int setUserGroupChatLastTime(GroupChat gc);
	int updateUserGroupChatLastTime(GroupChat gc);
	int checkUGCTime(GroupChat gc);
	String getUserGroupChatLastTime(GroupChat gc);
}
