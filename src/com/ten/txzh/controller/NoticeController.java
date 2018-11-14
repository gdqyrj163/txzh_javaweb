package com.ten.txzh.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

@Controller
public class NoticeController {
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private NoticeService noticeService;
	
	@ResponseBody
	@RequestMapping(value = "/joinGroupNotice", method = RequestMethod.POST, consumes = "application/json")
	public String joinGroupHandle(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		String groupid = map.get("groupid").toString();
		String noticeid = map.get("noticeid").toString();
		int result = Integer.parseInt(map.get("handle").toString());
		Map<String, String> resultMap = new HashMap<String, String>();
		
		GroupNotice notice = new GroupNotice();
		notice.setNoticeid(Integer.parseInt(noticeid));
		notice.setResult(result);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		notice.setTime(sdf.format(new Date()));
		if(noticeService.MessageHandle(notice) == 1) {
			if(notice.getResult() == 1) {
				Group_User group_user = new Group_User();
				group_user.setGroupid(Integer.parseInt(groupid));
				group_user.setUserid(Integer.parseInt(userid));
				if(groupService.JoinGroup(group_user) == 1) {
					resultMap.put("resultCode", "1");
				}else if(groupService.JoinGroup(group_user) == -1) {
					resultMap.put("resultCode", "-1");
				}else {
					resultMap.put("resultCode", "0");
				}
			}
		}
		
		Gson gson = new Gson();
		return gson.toJson(resultMap);
	}
}
