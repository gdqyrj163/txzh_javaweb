package com.ten.txzh.pojo;

import java.sql.Time;

public class PrivateChat {
	private int aimid;
	private int userid;
	private String content;
	private Time lasttime;
	
	
	
	public int getAimid() {
		return aimid;
	}
	public void setAimid(int aimid) {
		this.aimid = aimid;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Time getLasttime() {
		return lasttime;
	}
	public void setLasttime(Time lasttime) {
		this.lasttime = lasttime;
	}
	
	
}
