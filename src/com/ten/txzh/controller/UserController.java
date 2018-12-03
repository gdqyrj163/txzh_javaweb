package com.ten.txzh.controller;

import java.util.HashMap;
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
import com.ten.txzh.service.CheckCodeService;
import com.ten.txzh.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private CheckCodeService ccService;
	
	@ResponseBody
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String getUserInfo(@RequestBody Map map, HttpServletResponse response) {
		String userid = map.get("userid").toString();
		Map<String, String> resultMap = new HashMap<String, String>();
		User user = new User();
		
		user = userService.getUserInfo(Integer.parseInt(userid));
		if(user != null) {
			resultMap.put("resultCode", "1");
			resultMap.put("username", user.getUsername());
			resultMap.put("email", user.getEmail());
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
	@RequestMapping(value = "/alterUserInfo", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
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
	
	@ResponseBody
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String changePassword(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		String checkCode = map.get("checkCode").toString();
		String newPassword = map.get("newPassword").toString();
		String email = map.get("email").toString();
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(ccService.compareCheckCode(email, checkCode)) {
			User user = new User();
			user.setUserid(Integer.parseInt(userid));
			user.setPassword(newPassword);
			if(userService.changePassword(user) > 0) {
				resultMap.put("resultCode", "1");
			}else {
				resultMap.put("resultCode", "0");
			}
		}else {
			resultMap.put("resultCode", "-1");
		}
		Gson gson = new Gson();
		System.out.println("User has been changed his/her password");
		return gson.toJson(resultMap);
	}
	
	@ResponseBody
	@RequestMapping(value = "/findPasswordCheck", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String findPasswordCheck(@RequestBody Map map) {
		String email = map.get("email").toString();
		String checkCode = map.get("checkCode").toString();
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(ccService.compareCheckCode(email, checkCode)) {
			int userid = userService.getUseridByEmail(email);
			resultMap.put("userid", userid+"");
			resultMap.put("resultCode", "1");
		}else {
			resultMap.put("resultCode", "0");
		}
		
		Gson gson = new Gson();
		System.out.println("Find password: check email and checkCode(" + resultMap.get("resultCode") + ").");
		return gson.toJson(resultMap);
	}
	
	@ResponseBody
	@RequestMapping(value = "/findPassword", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String findPassword(@RequestBody Map map) {
		String userid = map.get("userid").toString();
		String newPassword = map.get("newPassword").toString();
		Map<String, String> resultMap = new HashMap<String, String>();
		Gson gson = new Gson();
		User user = new User();
		
		user.setUserid(Integer.parseInt(userid));
		user.setPassword(newPassword);
		if(userService.changePassword(user) > 0) {
			resultMap.put("resultCode", "1");
		}else {
			resultMap.put("resultCode", "0");
		}
		
		return gson.toJson(resultMap);
	}
}
