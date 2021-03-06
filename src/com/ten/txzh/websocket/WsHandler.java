package com.ten.txzh.websocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.ten.txzh.pojo.User;
import com.ten.txzh.service.UserService;
import com.ten.txzh.util.ServiceUtils;

public class WsHandler {
	private String indexPath;
	private UserService userService = (UserService)ServiceUtils.getBean(UserService.class);
	
	public WsHandler() {
		indexPath = this.getClass().getResource("/").toString();
		indexPath = indexPath.substring(indexPath.indexOf("/") + 1, indexPath.lastIndexOf("classes"));
		indexPath = indexPath + "GroupChat/";
	}
	
	public void saveGroupChat(String userid, String groupid, String message, String time) throws IOException {
		String groupPath = indexPath + groupid + "/";
		File chatPath = new File(groupPath);
		File chatFile = new File(groupPath + groupid + ".txzh");
		if(!chatPath.isDirectory()) {
			chatPath.mkdirs();
		}
		if(!chatFile.exists()) {
			chatFile.createNewFile();
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(chatFile, true));
		bw.write(userid + "@i#" + message + "@i#" + time);
		bw.newLine();
		bw.close();
		
	}
	
	public String getGroupChatLine(String time, String groupid) throws IOException {
		String groupPath = indexPath + groupid + "/";
		File chatPath = new File(groupPath);
		File chatFile = new File(groupPath + groupid + ".txzh");
		if(!chatPath.isDirectory()) {
			chatPath.mkdirs();
		}
		if(!chatFile.exists()) {
			chatFile.createNewFile();
		}
		System.out.println("Last time is:" + time);
		Map<String, List<String>> msgMap = new HashMap<String, List<String>>();
		BufferedReader br = new BufferedReader(new FileReader(chatFile));
		String line = null;
		while((line = br.readLine()) != null) {
			String[] items = line.split("@i#");
			String lineTime = items[2];
			BigInteger timeInt = new BigInteger(time);
			BigInteger lineTimeInt = new BigInteger(lineTime);
			if(timeInt.compareTo(lineTimeInt) == -1) {
				String userid = items[0];
				User user = new User();
				user = userService.getUserInfo(Integer.parseInt(userid.trim()));
				
				List<String> msgList = new ArrayList<String>();
				msgList.add(userid);
				msgList.add(user.getUsername());
				msgList.add(items[1]);
				msgList.add(user.getImage());
				msgMap.put(msgMap.size()+"", msgList);
				System.out.println("-----------Send unknown message:" + items[1]);
			}else {
				continue;
			}
		}
		Gson gson = new Gson();
		return gson.toJson(msgMap);
	}
	
	public String readLastLine(String groupid, String charset) throws IOException {
		String groupPath = indexPath + groupid + "/";
		File file = new File(groupPath + groupid + ".txzh");
	    if (!file.exists() || file.isDirectory() || !file.canRead()) {
	        return null;
	    }
	    RandomAccessFile raf = null;
	    try {
	        raf = new RandomAccessFile(file, "r");
	        long len = raf.length();
	        if (len == 0L) {
	            return "";
	        } else {
	            long pos = len - 1;
	            while (pos > 0) {
	                pos--;
	                raf.seek(pos);
	                if (raf.readByte() == '\n') {
	                    break;
	                }
	            }
	            if (pos == 0) {
	                raf.seek(0);
	            }
	            byte[] bytes = new byte[(int) (len - pos)];
	            raf.read(bytes);
	            if (charset == null) {
	                return new String(bytes);
	            } else {
	                return new String(bytes, charset);
	            }
	        }
	    } catch (FileNotFoundException e) {
	    } finally {
	        if (raf != null) {
	            try {
	                raf.close();
	            } catch (Exception e2) {
	            }
	        }
	    }
	    return null;
	}
}
