package com.ten.txzh.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ten.txzh.pojo.GroupChat;
import com.ten.txzh.service.GroupChatService;
import com.ten.txzh.websocket.WsHandler;

@Controller
public class ChatController {
	@Autowired
	GroupChatService gcService;
	
	@ResponseBody
	@RequestMapping(value = "/setLastChatTime", method = RequestMethod.POST, consumes = "application/json")
	public void setListChatTime(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		String groupid = map.get("groupid").toString();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
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
	@RequestMapping(value = "/getUnknownChat", method = RequestMethod.POST, consumes = "application/json")
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
}
