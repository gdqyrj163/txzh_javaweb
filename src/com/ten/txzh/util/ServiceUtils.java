package com.ten.txzh.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ten.txzh.service.UserService;

public class ServiceUtils implements ApplicationContextAware{
	
	private static ApplicationContext appCtx;
	 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		appCtx = applicationContext;
	}
	
	public static ApplicationContext getApplicationContext() {
        return appCtx;
    }
	
	public static Object getBean(Class<UserService> classbean) {
        return appCtx.getBean(classbean);
    }
	
}
