package com.LockerRoom.Client;
import java.io.*;
import java.net.*;
import java.util.*;

public class LockerRoomClient implements Runnable {

	private String username;
	private Socket socket;
	private PrintWriter writer;
	private static ClientArrayList<LockerRoomClient> lrcList;

	
	public LockerRoomClient(String n) {
		this.setUsername(n);
		if (lrcList == null) lrcList = new ClientArrayList<LockerRoomClient>();
		lrcList.threadSafeAdd(this);
	}

	@Override
	public void run() {
		connectToNetwork();
		sendMessage(getMessage());
		
	}
	
	public void connectToNetwork(){
		try {
			socket = new Socket("localhost", 5000); //Connect to my Linux server for chat functionality 192.168.1.175
													//or localhost Tomcat during testing
			System.out.println("Client is connected to server");
			writer = new PrintWriter(socket.getOutputStream());
			//Set writer's output to the socket's output stream
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public String getMessage(){  //temp class for testing Needs to modify to incorporate web code
		return "";
	}
	
	public void sendMessage(String message){ //temp class for testing
		writer.println(message);
		writer.flush();
	}
	
	
	public static ClientArrayList<LockerRoomClient> getLrcList() {
		return lrcList;
	}

	public static void setLrcList(ClientArrayList<LockerRoomClient> lrcList) {
		LockerRoomClient.lrcList = lrcList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

}
