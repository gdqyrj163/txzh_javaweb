package com.ten.txzh.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.ten.txzh.pojo.User;
import com.ten.txzh.service.UserService;
import com.ten.txzh.util.ServiceUtils;

@ServerEndpoint("/groupchat/{roomName}/{userid}")
public class WsServer {
	private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap<String, Set<Session>>();
	private static final List<Map<Session, List<String>>> speakers = new ArrayList<Map<Session, List<String>>>();
	
	UserService userService = (UserService)ServiceUtils.getBean(UserService.class);
	
	@OnOpen
	public void connnect(@PathParam("roomName") String roomName, @PathParam("userid") String userid, Session session) throws Exception{
		if(!rooms.containsKey(roomName)) {
			//保存聊天房间与session的关系
			Set<Session> room = new HashSet<>();
			room.add(session);
			rooms.put(roomName, room);
			//获取User
			User user = new User();
			System.out.println(Integer.parseInt(userid));
			user = userService.getUserInfo(Integer.parseInt(userid));
			//保存session与用户信息的关系
			Map<Session, List<String>> speaker = new HashMap<Session, List<String>>();
			List<String> speakerInfo = new ArrayList<String>();
			speakerInfo.add(String.valueOf(user.getUserid()));
			speakerInfo.add(user.getUsername());
			speakerInfo.add(user.getImage());
			speaker.put(session, speakerInfo);
			speakers.add(speaker);
		}else {
			//保存聊天房间与session的关系
			rooms.get(roomName).add(session);
			//获取User
			User user = new User();
			user = userService.getUserInfo(Integer.parseInt(userid));
			//保存session与用户信息的关系
			Map<Session, List<String>> speaker = new HashMap<Session, List<String>>();
			List<String> speakerInfo = new ArrayList<String>();
			speakerInfo.add(String.valueOf(user.getUserid()));
			speakerInfo.add(user.getUsername());
			speakerInfo.add(user.getImage());
			speaker.put(session, speakerInfo);
			speakers.add(speaker);
		}
		System.out.println("Client " + session.getId() + "(userid=" + userid + ") connected!");
	}
	
	@OnClose
	public void disConnect(@PathParam("roomName") String roomName, Session session) throws Exception{
		rooms.get(roomName).remove(session);
		for(int i = 0; i < speakers.size(); i++) {
			Map<Session, List<String>> speaker = speakers.get(i);
			for(Session checkSession : speaker.keySet()) {
				if(checkSession == session) {
					speakers.remove(i);
					break;
				}
			}
		}
		System.out.println("Client " + session.getId() + " disConnected!");
	}
	
	@OnMessage
	public void broadcastMessage(@PathParam("roomName") String roomName, String msg, Session session) throws Exception{
		broadcast(roomName, msg, session);
		System.out.println(session.getId() + " send message to " + roomName + " with message:" + msg);
	}
	
	@OnError
	public void errorHandle(Session session, Throwable error) {
		if(error.getMessage().contentEquals("java.io.EOFException")) {
			System.out.println(session.getId() + " has some problems, it will be close for moment.");
		}
	}
	
	public static void broadcast(String roomName, String msg, Session sourceSession) throws Exception {
		for(Session session : rooms.get(roomName)){
			if(session != sourceSession) {
				Gson gson = new Gson();
				List<String> msgList = new ArrayList<String>();
				List<String> speakerInfo = new ArrayList<String>();
				for(int i = 0; i < speakers.size(); i++) {
					Map<Session, List<String>> speaker = speakers.get(i);
					for(Session checkSession : speaker.keySet()) {
						if(checkSession == session) {
							speakerInfo = speaker.get(checkSession);
							break;
						}
					}
				}
				msgList.add(speakerInfo.get(0));
				msgList.add(speakerInfo.get(1));
				msgList.add(msg);
				msgList.add(speakerInfo.get(2));
				Map<String, List<String>> msgMap = new HashMap<String, List<String>>();
				msgMap.put("return", msgList);
				session.getBasicRemote().sendText(gson.toJson(msgMap));
			}
		}
	}
}
