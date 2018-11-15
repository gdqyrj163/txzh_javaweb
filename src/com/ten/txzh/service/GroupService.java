package com.ten.txzh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.GroupMapper;
import com.ten.txzh.pojo.Group;
import com.ten.txzh.pojo.Group_User;

@Service
public class GroupService {
	
	@Autowired
	GroupMapper groupDao;
	
	public int CreateGroup(Group group) {
		int groupid = 0;
		groupid = groupDao.CreateGroup(group);
		System.out.println("Create Group Success:" + groupid);
		return groupid;
	}
	
	public int JoinGroup(Group_User group_user) {
		int resultCode = 0;
		if(groupDao.JoinGroupCheck(group_user) == 0) {
			System.out.println("User already to join in.");
			if(groupDao.JoinGroup(group_user) > 0) {
				resultCode = 1;
			}
		}else {
			System.out.println("User has been member of this group!");
			resultCode = -1;
		}
		return resultCode;
	}
	
	@SuppressWarnings("finally")
	public List<Group> searchGroup(String searchGroupValue){
		List<Group> groupList = new ArrayList<Group>();
		try {
			if(searchGroupValue.matches("[0-9]+")) {
				System.out.println("Search Group By Integer.");
				groupList = groupDao.searchGroupByNum(Integer.parseInt(searchGroupValue));
			}else {
				System.out.println("Search Group By String.");
				groupList = groupDao.searchGroup(searchGroupValue);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			return groupList;
		}
	}
	
	public int getGroupMaster(int groupid) {
		int groupMaster = 0;
		groupMaster = groupDao.getGroupMaster(groupid);
		return groupMaster;
	}
	
	public int kickUser(Group_User group_user) {
		int resultCode = 0;
		if(groupDao.kickUser(group_user) > 0) {
			resultCode = 1;
		}
		return resultCode;
	}
	
	@SuppressWarnings("finally")
	public Group getGroupInfo(int groupid) {
		Group group = new Group();
		try {
			group = groupDao.getGroupInfo(groupid);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			return group;
		}
	}
	
	public int joinGroupCheck(Group_User group_user) {
		int resultCode = 0;
		if(groupDao.JoinGroupCheck(group_user) == 0) {
			resultCode = 1;
		}
		return resultCode;
	}
	
	public int getMembersNum(int groupid) {
		int membersNum = 0;
		membersNum = groupDao.getGroupMembersNumber(groupid);
		return membersNum;
	}
}
