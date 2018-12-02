package com.ten.txzh.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ten.txzh.pojo.User;
import com.ten.txzh.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST, consumes = "application/json")
	public String getUserInfo(@RequestBody Map map, HttpServletResponse response) {
		String userid = map.get("userid").toString();
		Map<String, String> resultMap = new HashMap<String, String>();
		User user = new User();
		
		user = userService.getUserInfo(Integer.parseInt(userid));
		if(user != null) {
			resultMap.put("resultCode", "1");
			resultMap.put("username", user.getUsername());
			resultMap.put("address", user.getAddress());
			resultMap.put("image", user.getImage());
		}else {
			resultMap.put("resultCode", "0");
		}
		
		Gson gson = new Gson();
		System.out.println(userid + " Get UserInfo");
		response.setHeader("content-type", "text/html;charset=utf-8");
		return gson.toJson(resultMap);
	}
	
	@ResponseBody
	@RequestMapping(value = "/alterUserInfo", method = RequestMethod.POST, consumes = "application/json")
	public String alterUserInfo(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		String username = map.get("username").toString();
		String address = map.get("address").toString();
		String image = map.get("image").toString();
		Map<String, String> resultMap = new HashMap<String, String>();
		Gson gson = new Gson();
		
		User user = new User();
		user.setUserid(Integer.parseInt(userid));
		user.setUsername(username);
		user.setAddress(address);
		user.setImage(image);
		
		if(userService.alterUserInfo(user) > 0) {
			resultMap.put("resultCode", "1");
		}else {
			resultMap.put("resultCode", "0");
		}
		
		System.out.println(userid + " alter his/her info.");
		return gson.toJson(resultMap);
	}
}
