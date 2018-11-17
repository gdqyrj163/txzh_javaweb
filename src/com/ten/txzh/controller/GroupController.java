package com.ten.txzh.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
	
	
	@ResponseBody
	@RequestMapping(value = "/getGroups", method = RequestMethod.POST, consumes = "application/json")
	public String getGroups(@RequestBody Map map) {
		Gson gson = new Gson();
		String userid = map.get("userid").toString();
		List<Group_User> groups = new ArrayList<Group_User>();
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		
		groups = groupService.getGroups(Integer.parseInt(userid));
		for(Group_User eachGroup: groups) {
			List<String> eachList = new ArrayList<String>();
			Group groupinfo = eachGroup.getGroup();
			eachList.add(String.valueOf(eachGroup.getGroupid()));
			eachList.add(groupinfo.getName());
			eachList.add(groupinfo.getImage());
			resultMap.put(String.valueOf(eachGroup.getGroupid()), eachList);
		}
		
		
		System.out.println("Get Groups.");
		return gson.toJson(resultMap);
	}
	
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
		
		Group_User group_joiner = new Group_User();
		group_joiner.setGroupid(Integer.parseInt(groupid));
		group_joiner.setUserid(Integer.parseInt(userid));
		if(groupService.joinGroupCheck(group_joiner) == 1) {
			System.out.println("System send a message to this user.");
			if(noticeService.JoinMessage(joinNotice) == 1) {
				resultMap.put("resultCode", "1");
			}else {
				resultMap.put("resultCode", "0");
			}
		}else {
			System.out.println("This user has been join in this group!");
			resultMap.put("resultCode", "-1");
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
	
	@ResponseBody
	@RequestMapping(value = "/getGroupMembers", method = RequestMethod.POST, consumes = "application/json")
	public String getGroupMembers(@RequestBody Map map) {
		String groupid = map.get("groupid").toString();
		Map<String, List<String>> resultMembers = new HashMap<String, List<String>>();
		List<Group_User> userList = new ArrayList<Group_User>();
		Gson gson = new Gson();
		System.out.println("groupid is:" + groupid);
		
		userList = groupService.getGroupUsers(Integer.parseInt(groupid));
		for(Group_User group_member: userList) {
			List<String> eachUser = new ArrayList<String>();
			User user = new User();
			user = group_member.getUser();
			eachUser.add(String.valueOf(group_member.getUserid()));
			eachUser.add(user.getUsername());
			eachUser.add(user.getImage());
			resultMembers.put(String.valueOf(group_member.getUserid()), eachUser);
		}
		
		return gson.toJson(resultMembers);
	}
	
	@ResponseBody
	@RequestMapping(value = "/kickGroupMember", method = RequestMethod.POST, consumes = "application/json")
	public String kickGroupMember(@RequestBody Map map) {
		String groupid = map.get("groupid").toString();
		String userid = map.get("userid").toString();
		Map<String, String> resultMap = new HashMap<String, String>();
		Group_User group_kick = new Group_User();
		Gson gson = new Gson();
		
		group_kick.setGroupid(Integer.parseInt(groupid));
		group_kick.setUserid(Integer.parseInt(userid));
		
		if(groupService.kickGroupMemberCheck(group_kick) == 0) {
			if(groupService.kickUser(group_kick) == 1) {
				GroupNotice notice = new GroupNotice();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				notice.setUserid(Integer.parseInt(userid));
				notice.setOperation(0);
				notice.setType(3);
				notice.setSource(groupid);
				notice.setTarget(userid);
				notice.setResult(1);
				notice.setTime(sdf.format(new Date()));
				resultMap.put("resultCode", "1");
			}else {
				resultMap.put("resultCode", "0");
			}
		}else {
			resultMap.put("resultCode", "-1");
		}
			System.out.println("kick " + userid + " from " + groupid + ",resultCode:" + resultMap.get("resultCode"));
			return gson.toJson(resultMap);
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
