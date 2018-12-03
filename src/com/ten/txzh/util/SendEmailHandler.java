package com.ten.txzh.util;

import org.apache.commons.mail.HtmlEmail;

public class SendEmailHandler {
	private static String hostName = "smtp.163.com";
	private static String charset = "UTF-8";
	private static String from = "kadoceex@163.com";
	private static String authCode = "mxd7520863";
	
	public static String createCheckCode() {
		String checkCode = "";
		for(int i = 0; i < 6; i++) {
			int num = (int) (Math.random()*10);
			checkCode = checkCode + num;
		}
		return checkCode;
	}
	
	public static boolean sendEmail(String email, String checkCode){
		try {
			HtmlEmail he = new HtmlEmail();
			he.setHostName(hostName);
			he.setCharset(charset);
			he.addTo(email);
			he.setFrom(from, "卡蛋法");
			he.setAuthentication(from, authCode);
			
			he.setSubject("天下纵横第十组不太厉害组织");
			he.setMsg("您的验证码为：" + checkCode);
			
			he.send();
			System.out.println("Send checkCode to user email");
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
