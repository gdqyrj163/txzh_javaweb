package com.ten.txzh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
