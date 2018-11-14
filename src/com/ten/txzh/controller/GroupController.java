package com.ten.txzh.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ten.txzh.pojo.Group;
import com.ten.txzh.pojo.GroupNotice;
import com.ten.txzh.pojo.Group_User;
import com.ten.txzh.pojo.User;
import com.ten.txzh.service.GroupService;
import com.ten.txzh.service.NoticeService;
import com.ten.txzh.service.UserService;

@Controller
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserService userService;
	
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/createGroup", method = RequestMethod.POST, consumes = "application/json")
	public String createGroup(@RequestBody Map map){
		Gson gson = new Gson();
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String name = map.get("name").toString();
		String introduce = map.get("introduce").toString();
		String master = map.get("master").toString();
		String image = map.get("image").toString();
		
		Group newGroup = new Group();
		newGroup.setName(name);
		newGroup.setIntroduce(introduce);
		newGroup.setMaster(Integer.parseInt(master));
		newGroup.setImage(image);
		
		int groupid = groupService.CreateGroup(newGroup);
		if(groupid != 0) {
			Group_User group_master = new Group_User();
			group_master.setGroupid(groupid);
			group_master.setUserid(Integer.parseInt(master));
			groupService.JoinGroup(group_master);
			resultMap.put("resultCode", "1");
		}else {
			resultMap.put("resultCode", "0");
		}
		
		System.out.println("CreateGroup:" + resultMap.get("resultCode"));
		return gson.toJson(resultMap);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/searchGroup", method = RequestMethod.POST, consumes = "application/json")
	public String searchGroup(@RequestBody Map map) {
		String searchGroupValue = map.get("searchGroupValue").toString();
		List<Group> groupList = new ArrayList<Group>();
		groupList = groupService.searchGroup(searchGroupValue);
		
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		
		for(Group group:groupList) {
			List<String> groupEachValue = new ArrayList<String>();
			groupEachValue.add(String.valueOf(group.getGroupid()));
			groupEachValue.add(group.getName());
			groupEachValue.add(group.getImage());
			resultMap.put(String.valueOf(group.getGroupid()), groupEachValue);
		}
		
		Gson gson = new Gson();
		
//		System.out.println("searchKey:" + searchGroupValue + ", getValue:" + gson.toJson(resultMap));
		return gson.toJson(resultMap);
		
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/joinGroup", method = RequestMethod.POST, consumes = "application/json")
	public String joinGroup(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		String groupid = map.get("groupid").toString();
		int groupMaster = groupService.getGroupMaster(Integer.parseInt(groupid));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Gson gson = new Gson();
		Map<String, String> resultMap = new HashMap<String, String>();
		
		GroupNotice joinNotice = new GroupNotice();
		joinNotice.setUserid(groupMaster);
		joinNotice.setType(0);
		joinNotice.setOperation(1);
		joinNotice.setSource(userid);
		joinNotice.setTarget(groupid);
		joinNotice.setResult(2);
		joinNotice.setTime(sdf.format(new Date()));
		if(noticeService.JoinMessage(joinNotice) == 1) {
			resultMap.put("resultCode", "1");
		}else {
			resultMap.put("resultCode", "0");
		}
		
		return gson.toJson(resultMap);
	}
	
	@SuppressWarnings("finally")
	@ResponseBody
	@RequestMapping(value = "/getGroupInfo", method = RequestMethod.POST, consumes = "application/json")
	public String getGroupInfo(@RequestBody Map map) {
		Map<String, String> resultMap = new HashMap<String, String>();
		int groupid = Integer.parseInt(map.get("groupid").toString());
		
		Group group = new Group();
		group = groupService.getGroupInfo(groupid);
		int membersNum = groupService.getMembersNum(groupid);
		User master = new User();
		master = userService.getUserInfo(group.getMaster());
		try {
			resultMap.put("groupid", group.getGroupid() + "");
			resultMap.put("name", group.getName());
			resultMap.put("introduce", group.getIntroduce());
			resultMap.put("master", group.getMaster() + "");
			resultMap.put("masterName", master.getUsername());
			resultMap.put("image", group.getImage());
			resultMap.put("membersNum", String.valueOf(membersNum));
			resultMap.put("resultCode", "1");
		}catch(Exception e) {
			resultMap.put("resultCode", "0");
		}finally {
			Gson gson = new Gson();
			
			return gson.toJson(resultMap);
		}
	}
	
	public Group createGroup_setGroup(Group group, String filedName, String filedValue) {
		switch(filedName) {
			case "name":
				group.setName(filedValue);
				break;
			case "introduce":
				group.setIntroduce(filedValue);
				break;
			case "master":
				group.setMaster(Integer.parseInt(filedValue));
				break;
			case "image":
				group.setImage(filedValue);
				break;
		}
		return group;
	}
}
