package com.ten.txzh.dao;

import java.util.List;

import com.ten.txzh.pojo.Group;
import com.ten.txzh.pojo.Group_User;

public interface GroupMapper {
	List<String> getGroupUsers(int groupid);
	int CreateGroup(Group group);
	int JoinGroup(Group_User group_user);
	int JoinGroupCheck(Group_User group_user);
	List<Group> searchGroup(String searchGroupValue);
	int getGroupMaster(int groupid);
	int kickUser(Group_User group_user);
}