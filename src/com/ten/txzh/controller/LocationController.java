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
import com.ten.txzh.pojo.Maps;
import com.ten.txzh.pojo.User;
import com.ten.txzh.service.LocationService;
import com.ten.txzh.service.UserService;

@Controller
public class LocationController {
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value = "/catchGPS", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String getGPS(@RequestBody Map map) {
		Maps userLoc = new Maps();
		userLoc.setUserid(Integer.parseInt(map.get("userid").toString()));
		userLoc.setLongtude(Float.parseFloat(map.get("longtude").toString()));
		userLoc.setLatitude(Float.parseFloat(map.get("latitude").toString()));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		userLoc.setTime(format.format(new Date()));
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		if(locationService.setLocation(userLoc) == 1) {
			resultMap.put("resultCode", "1");
		}
		
		Gson gson = new Gson();
		
		//System.out.println("(" + userLoc.getLongtude() + ", " + userLoc.getLatitude() + ")"); 
		
		return gson.toJson(resultMap);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getGroupLocation", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String getGroupLocation(@RequestBody Map map) {
		String groupid = map.get("groupid").toString();
		String userid = map.get("userid").toString();
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		Gson gson = new Gson();
		List<Maps> groupLoc = new ArrayList<Maps>();
		
		groupLoc = locationService.getGroupLocation(Integer.parseInt(groupid), Integer.parseInt(userid));
		for(Maps eachMap: groupLoc) {
			String username = userService.getUserName(eachMap.getUserid());
			List<String> locList = new ArrayList<String>();
			locList.add(String.valueOf(eachMap.getUserid()));
			locList.add(username);
			locList.add(String.valueOf(eachMap.getLongtude()));
			locList.add(String.valueOf(eachMap.getLatitude()));
			resultMap.put(String.valueOf(eachMap.getUserid()), locList);
		}
		return gson.toJson(resultMap);
	}
}
