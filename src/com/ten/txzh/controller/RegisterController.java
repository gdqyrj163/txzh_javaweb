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
import com.ten.txzh.pojo.Maps;
import com.ten.txzh.pojo.User;
import com.ten.txzh.service.LoginService;
import com.ten.txzh.service.RegisterService;

@Controller
public class RegisterController {
	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private LoginService loginService;
	
	@ResponseBody
	@RequestMapping(value = "/reg", method = RequestMethod.POST, consumes = "application/json")
	public String register(@RequestBody Map map) {
		User regUser = new User();
		regUser.setUsername(map.get("username").toString());
		regUser.setEmail(map.get("email").toString());
		regUser.setPassword(map.get("password").toString());
		regUser.setImage(map.get("image").toString());
		
		int checkCode = Integer.parseInt(map.get("checkCode").toString());
		int resultCode = 0;
		if(checkCode == 123456) {
			resultCode = registerService.doReg(regUser);
		}
		
		Map<String, String> resultMap = new HashMap<String, String>();
		if(resultCode == 1) {
			User user = new User();
			user = loginService.LoginCheck(regUser);
			resultMap.put("resultCode", String.valueOf(resultCode));
			resultMap.put("userid", String.valueOf(user.getUserid()));
			resultMap.put("username", user.getUsername());
			resultMap.put("email", user.getEmail());
			resultMap.put("address", user.getAddress());
			resultMap.put("image", user.getImage());
			
			Maps userLoc = new Maps();
			userLoc.setUserid(user.getUserid());
			userLoc.setLongtude(0);
			userLoc.setLatitude(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			userLoc.setTime(sdf.format(new Date()));
		}else {
			resultMap.put("resultCode", String.valueOf(resultCode));
		}
		
		Gson gson = new Gson();
		System.out.println(resultMap);
		return gson.toJson(resultMap);
	}
}
