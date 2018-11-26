package com.ten.txzh.websocket;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/groupchat/{roomName}")
public class WsServer {
	private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap<String, Set<Session>>();
	
	@OnOpen
	public void connnect(@PathParam("roomName") String roomName, Session session) throws Exception{
		if(!rooms.containsKey(roomName)) {
			Set<Session> room = new HashSet<>();
			room.add(session);
			rooms.put(roomName, room);
		}else {
			rooms.get(roomName).add(session);
		}
		System.out.println("Client " + session.getId() + " connected!");
	}
	
	@OnClose
	public void disConnect(@PathParam("roomName") String roomName, Session session) throws Exception{
		rooms.get(roomName).remove(session);
		System.out.println("Client" + session.getId() + " disConnected!");
	}
	
	@OnMessage
	public void broadcastMessage(@PathParam("roomName") String roomName, String msg, Session session) throws Exception{
		broadcast(roomName, msg, session);
		System.out.println(session.getId() + " send message to " + roomName + " with message:" + msg);
	}
	
	public static void broadcast(String roomName, String msg, Session sourceSession) throws Exception {
		for(Session session : rooms.get(roomName)){
			if(session != sourceSession) {
				session.getBasicRemote().sendText(msg);
			}
		}
	}
}
