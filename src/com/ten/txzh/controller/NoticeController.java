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
import com.ten.txzh.pojo.GroupNotice;
import com.ten.txzh.pojo.Group_User;
import com.ten.txzh.service.GroupService;
import com.ten.txzh.service.NoticeService;
import com.ten.txzh.service.UserService;

@Controller
public class NoticeController {
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value = "/getGroupNotice", method = RequestMethod.POST, consumes = "application/json")
	public String getGroupNotice(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		List<GroupNotice> noticeList = new ArrayList<GroupNotice>();
		Map<String, List<String>> noticeListMap = new HashMap<String, List<String>>();
		
		noticeList = noticeService.getGroupNotice(Integer.parseInt(userid));
		for(GroupNotice notice: noticeList) {
			String sourceName = "";
			String targetName = "";
			List<String> noticeInfo = new ArrayList<String>();
			noticeInfo.add(String.valueOf(notice.getNoticeid()));
			noticeInfo.add(String.valueOf(notice.getType()));
			noticeInfo.add(String.valueOf(notice.getOperation()));
			if((notice.getType() == 0) || (notice.getType() == 2)) {
				//获取请求发出者或群信息 User to Group
				int source = Integer.parseInt(notice.getSource());
				sourceName = userService.getUserName(source);
				//获取请求接收者或群信息
				int target = Integer.parseInt(notice.getTarget());
				targetName = groupService.getGroupName(target);
			}else {
				//获取请求发出者或群信息 Group to User
				int source = Integer.parseInt(notice.getSource());
				sourceName = groupService.getGroupName(source);
				//获取请求接收者或群信息
				int target = Integer.parseInt(notice.getTarget());
				targetName = userService.getUserName(target);
				
			}
			noticeInfo.add(sourceName);
			noticeInfo.add(String.valueOf(notice.getResult()));
			noticeInfo.add(targetName);
			noticeListMap.put(String.valueOf(notice.getNoticeid()), noticeInfo);
		}
		
		Gson gson = new Gson();
		System.out.println("Get User Notices");
		return gson.toJson(noticeListMap);
	}
	
	@ResponseBody
	@RequestMapping(value = "/joinGroupNotice", method = RequestMethod.POST, consumes = "application/json")
	public String joinGroupHandle(@RequestBody Map map) {
		String noticeid = map.get("noticeid").toString();
		int result = Integer.parseInt(map.get("result").toString());
		Map<String, String> resultMap = new HashMap<String, String>();
		
		GroupNotice notice = new GroupNotice();
		notice.setNoticeid(Integer.parseInt(noticeid));
		notice.setResult(result);
		notice.setOperation(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		notice.setTime(sdf.format(new Date()));
		
		if(noticeService.MessageHandle(notice) == 1) {
			if(notice.getResult() == 1) {
				GroupNotice handleNotice = new GroupNotice();
				handleNotice = noticeService.getNotice(Integer.parseInt(noticeid));
				Group_User group_joiner = new Group_User();
				if((handleNotice.getType() != 0) && (handleNotice.getType() != 2)) {
					//Group to User
					group_joiner.setGroupid(Integer.parseInt(handleNotice.getSource()));
					group_joiner.setUserid(Integer.parseInt(handleNotice.getTarget()));
				}else {
					//User to Group
					group_joiner.setGroupid(Integer.parseInt(handleNotice.getTarget()));
					group_joiner.setUserid(Integer.parseInt(handleNotice.getSource()));
				}
				int resultCode = groupService.JoinGroup(group_joiner);
				resultMap.put("resultCode", resultCode + "");
			}
			System.out.println("Apply Group Pass");
		}else {
			System.out.println("Apply Group No Pass");
		}
		Gson gson = new Gson();
		return gson.toJson(resultMap);
	}
}
