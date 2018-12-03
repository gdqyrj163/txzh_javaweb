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
import com.ten.txzh.pojo.CheckCode;
import com.ten.txzh.service.CheckCodeService;
import com.ten.txzh.util.SendEmailHandler;

@Controller
public class CheckCodeController {
	
	@Autowired
	CheckCodeService ccService;
	
	@ResponseBody
	@RequestMapping(value = "/setCheckCode", method = RequestMethod.POST, consumes = "application/json", produces = "text/html;charset=UTF-8")
	public String setCheckCode(@RequestBody Map map) {
		String email = map.get("email").toString();
		String checkCode = SendEmailHandler.createCheckCode();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String lastTime = sdf.format(new Date());
		Map<String, String> resultMap = new HashMap<String, String>();
		Gson gson = new Gson();
		
		CheckCode cc = new CheckCode();
		cc.setEmail(email);
		cc.setCheckCode(checkCode);
		cc.setTime(lastTime);
		
		if(!ccService.checkExist(email)) {
			ccService.saveCheckCode(cc);
		}else {
			if(ccService.checkTime(email, lastTime)) {
				ccService.deleteCheckCode(email);
				ccService.saveCheckCode(cc);
			}else {
				resultMap.put("resultCode", "-1");
				return gson.toJson(resultMap);
			}
		}
		
		if(SendEmailHandler.sendEmail(email, checkCode)) {
			resultMap.put("resultCode", "1");
			System.out.println("Send email success");
		}else {
			resultMap.put("resultCode", "0");
			System.out.println("Send email fail");
		}
		
		
		return gson.toJson(resultMap);
	}
}
