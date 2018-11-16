package com.ten.txzh.dao;

import java.util.List;

import com.ten.txzh.pojo.Group;
import com.ten.txzh.pojo.Group_User;
import com.ten.txzh.pojo.User;

public interface GroupMapper {
	List<Group_User> getGroups(int userid);
	List<Group_User> getGroupUsers(int groupid);
	List<String> getGroupUsersid(int groupid);
	int CreateGroup(Group group);
	int JoinGroup(Group_User group_user);
	int JoinGroupCheck(Group_User group_user);
	List<Group> searchGroup(String searchGroupValue);
	List<Group> searchGroupByNum(int serarchGroupValue);
	int getGroupMaster(int groupid);
	int kickUser(Group_User group_user);
	Group getGroupInfo(int groupid);
	int getGroupMembersNumber(int groupid);
	List<Group> getGroupByUserid(int userid);
	String getGroupNameByGroupid(int groupid);
}
