package com.ten.txzh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ten.txzh.dao.GroupMapper;
import com.ten.txzh.dao.NoticeMapper;
import com.ten.txzh.pojo.GroupNotice;

@Service
public class NoticeService {
	
	@Autowired
	NoticeMapper noticeDao;
	
	public int MessageHandle(GroupNotice notice) {
		int resultCode = 0;
		if(noticeDao.MessageHandle(notice) > 0) {
			resultCode = 1;
		}
		return resultCode;
	}
	
	public int JoinMessage(GroupNotice notice) {
		int resultCode = 0;
		if(noticeDao.JoinMessage(notice) == 1) {
			resultCode = 1;
		}
		return resultCode;
	}
	
	public List<GroupNotice> getGroupNotice(int userid){
		List<GroupNotice> noticeList = new ArrayList<GroupNotice>();
		noticeList = noticeDao.getGroupNotice(userid);
		return noticeList;
	}
	
	public GroupNotice getNotice(int noticeid) {
		GroupNotice notice = new GroupNotice();
		notice = noticeDao.getNoticeByNoticeid(noticeid);
		return notice;
	}
}
