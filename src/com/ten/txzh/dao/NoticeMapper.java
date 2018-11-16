package com.ten.txzh.dao;

import java.util.List;

import com.ten.txzh.pojo.GroupNotice;

public interface NoticeMapper {
	List<GroupNotice> getGroupNotice(int userid);
	GroupNotice getNoticeByNoticeid(int noticeid);
	int JoinMessage(GroupNotice notice);
	int MessageHandle(GroupNotice notice);
	int kickMessage(GroupNotice notice);
}
