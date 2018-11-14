package com.ten.txzh.dao;

import com.ten.txzh.pojo.GroupNotice;
import com.ten.txzh.pojo.Maps;

public interface NoticeMapper {
	int JoinMessage(GroupNotice notice);
	int MessageHandle(GroupNotice notice);
	int kickMessage(GroupNotice notice);
}
