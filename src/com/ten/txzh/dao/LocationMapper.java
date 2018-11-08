package com.ten.txzh.dao;

import com.ten.txzh.pojo.Maps;
import com.ten.txzh.pojo.User;

public interface LocationMapper {
	int setLocation(Maps user);
	
	int updateLocation(Maps userLoc);
	
	Maps getUserLocation (int userid);
	
	int locationCheck (int userid);
	
}
