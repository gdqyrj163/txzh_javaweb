package com.ten.txzh.controller;

import java.io.IOException;
import java.math.BigInteger;
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
import com.ten.txzh.pojo.GroupChat;
import com.ten.txzh.service.GroupChatService;
import com.ten.txzh.service.GroupService;
import com.ten.txzh.websocket.WsHandler;

@Controller
public class ChatController {
	@Autowired
	GroupChatService gcService;
	
	@Autowired
	GroupService groupService;
	
	@ResponseBody
	@RequestMapping(value = "/setLastChatTime", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public void setListChatTime(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		String groupid = map.get("groupid").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		
		GroupChat gc = new GroupChat();
		gc.setUserid(Integer.parseInt(userid));
		gc.setGroupid(Integer.parseInt(groupid));
		gc.setLasttime(time);
		if(gcService.setUserGroupChatLastTime(gc) > 0) {
			System.out.println(userid + " linked. To save groupchat(" + groupid + ") last time(" + time + ").");
		}else {
			System.out.println("Link groupchat fail.");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUnknownChat", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String getUnknownChat(@RequestBody Map map) throws IOException {
		String userid = map.get("userid").toString();
		String groupid = map.get("groupid").toString();
		GroupChat gc = new GroupChat();
		gc.setUserid(Integer.parseInt(userid));
		gc.setGroupid(Integer.parseInt(groupid));
		
		String lastTime = gcService.getUserGroupChatLastTime(gc);
		if(lastTime == null) {
			lastTime = "0";
		}
		WsHandler wsh = new WsHandler();
		
		System.out.println("Ready to read unknown message");
		return wsh.getGroupChatLine(lastTime, groupid);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getHistoryChat", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String getHistoryChat(@RequestBody Map map) throws IOException {
		String userid = map.get("userid").toString();
		WsHandler wsh = new WsHandler();
		List<GroupChat> gcList = new ArrayList<GroupChat>();
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		Gson gson = new Gson();
		
		gcList = gcService.getUserHistoryGroupChat(Integer.parseInt(userid));
		for(GroupChat gc : gcList) {
			int groupid = gc.getGroupid();
			Group group = groupService.getGroupInfo(groupid);
			
			String lastTime = gc.getLasttime();
			String lastLine = wsh.readLastLine(String.valueOf(groupid), "utf-8");
			if(lastLine.trim().equals("")) {
				continue;
			}
			
			String[] items = lastLine.split("@i#");
			BigInteger lastTimeInt = new BigInteger(lastTime);
			BigInteger saveTimeInt = new BigInteger(items[2].trim());
			
			String name = group.getName();
			String image = group.getImage();
			String lastMessage = items[1];
			String isNew = "0";
			if(lastTimeInt.compareTo(saveTimeInt) == -1) {
				isNew = "1";
			}
			
			List<String> eachList = new ArrayList<String>();
			eachList.add(String.valueOf(groupid));
			eachList.add(name);
			eachList.add(image);
			eachList.add(lastMessage);
			eachList.add(isNew);
			resultMap.put(String.valueOf(groupid), eachList);
		}
		
		
		System.out.println("User get chat history");
		return gson.toJson(resultMap);
	}
}
