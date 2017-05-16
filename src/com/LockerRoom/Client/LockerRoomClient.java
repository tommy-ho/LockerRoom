package com.LockerRoom.Client;
import java.io.*;
import java.net.*;
import java.util.*;

public class LockerRoomClient implements Runnable {

	private String username;
	private Socket socket;
	private PrintWriter writer;
	private InputStreamReader isReader;
	private BufferedReader reader;
	private ArrayList<String> messageBuffer;
	private static ClientArrayList<LockerRoomClient> lrcList;

	
	public LockerRoomClient(String n) {
		this.setUsername(n);
		if (lrcList == null) lrcList = new ClientArrayList<LockerRoomClient>();
		lrcList.threadSafeAdd(this);
		System.out.println("Client get initiated");
	}

	@Override
	public void run() {
		connectToNetwork();
		getMessageFromServer();
	}
	
	public void connectToNetwork(){
		try {
			socket = new Socket("localhost", 5000);
			//Connect to my Linux server for chat functionality 192.168.1.175
			//or localhost Tomcat server during testing
			System.out.println("Client is connected to server");
			writer = new PrintWriter(socket.getOutputStream());
			isReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isReader);
			messageBuffer = new ArrayList<String>();
			//Set writer's output to the socket's output stream
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void getMessageFromServlet(String message){
		//Servlet doPost packages message, calls the client (referenced through LRC List)
		sendMessageToServer(this, message);
	}
	
	private void sendMessageToServer(LockerRoomClient client, String message){ 
		writer.println(client.username + ": " + message);
		writer.flush(); //sends msg to server
	}
	
	public void getMessageFromServer(){
		String message;
		
		try {
			while ((message = reader.readLine()) != null){
				messageBuffer.add(message);
				
				if (messageBuffer.size() > 100){
					while (messageBuffer.size() > 80) messageBuffer.remove(0);
				}
				//send it back to servlet/jsp page -- This is the method that refreshes user JSP
				//sendMessageToServlet(message);
			}
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public void sendMessageToServlet(String message){
		//and prompt refresh
	}
	
	public ArrayList<String> getMessageBuffer(){
			return messageBuffer;
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
