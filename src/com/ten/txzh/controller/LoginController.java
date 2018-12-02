package com.ten.txzh.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ten.txzh.pojo.User;
import com.ten.txzh.service.LoginService;
import com.ten.txzh.websocket.WsHandler;

@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public String login(@RequestBody Map map) {
		User login_user = new User();
		login_user.setEmail(map.get("email").toString());
		login_user.setPassword(map.get("password").toString());
		
		User user = new User();
		user = loginService.LoginCheck(login_user);
		
		Gson gson = new Gson();
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(user != null) {
			resultMap.put("resultCode", "1");
			resultMap.put("userid", String.valueOf(user.getUserid()));
			resultMap.put("username", user.getUsername());
			resultMap.put("image", user.getImage());
		}else {
			resultMap.put("resultCode", "0");
		}
		System.out.println("User login:" + resultMap.get("userid"));
		return gson.toJson(resultMap);
	}
}
