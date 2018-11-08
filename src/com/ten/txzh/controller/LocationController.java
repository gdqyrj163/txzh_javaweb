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
import com.ten.txzh.service.LocationService;

@Controller
public class LocationController {
	@Autowired
	private LocationService locationService;
	
	@ResponseBody
	@RequestMapping(value = "/catchGPS", method = RequestMethod.POST, consumes = "application/json")
	public String getGPS(@RequestBody Map map) {
		Maps userLoc = new Maps();
		userLoc.setUserid(Integer.parseInt(map.get("userid").toString()));
		userLoc.setLongtude(Float.parseFloat(map.get("longtude").toString()));
		userLoc.setLatitude(Float.parseFloat(map.get("latitude").toString()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		userLoc.setTime(format.format(new Date()));
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		if(locationService.setLocation(userLoc) == 1) {
			resultMap.put("resultCode", "1");
		}
		
		Gson gson = new Gson();
		
		//System.out.println("(" + userLoc.getLongtude() + ", " + userLoc.getLatitude() + ")"); 
		
		return gson.toJson(resultMap);
	}
}
