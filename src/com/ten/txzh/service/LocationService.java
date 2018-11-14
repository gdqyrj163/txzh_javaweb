package com.ten.txzh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.GroupMapper;
import com.ten.txzh.dao.LocationMapper;
import com.ten.txzh.pojo.Maps;

@Service
public class LocationService {
	
	@Autowired
	LocationMapper locationDao;
	
	@Autowired
	GroupMapper groupDao;

	public int setLocation(Maps userLoc) {
		int ResultCode = 0;
		
		int userid = userLoc.getUserid();
		if(locationDao.locationCheck(userid) == 0) {
			if(locationDao.setLocation(userLoc) == 1) {
				ResultCode = 1;
			}
		}else {
			if(locationDao.updateLocation(userLoc) == 1) {
				ResultCode = 1;
			}
		}
		
		return ResultCode;
	}
	
	public List<Maps> getGroupLocation(int groupid){
		List<Maps> groupUsersLoc = new ArrayList<Maps>();
		List<String> groupUsers = groupDao.getGroupMembers(groupid);
		if(groupUsers.isEmpty() == false) {
			for(int i = 0; i < groupUsers.size(); i++) {
				groupUsersLoc.add(locationDao.getUserLocation(Integer.parseInt(groupUsers.get(i))));
			}
		}
		return groupUsersLoc;
	}
}
