package com.ten.txzh.pojo;

import java.sql.Time;

public class GroupChat {
	private int userid;
	private int groupid;
	private String content;
	private Time lasttime;
	
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
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
