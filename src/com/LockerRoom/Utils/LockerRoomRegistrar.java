package com.LockerRoom.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class LockerRoomRegistrar {
	
	private static ArrayList<String> loggedInUsers;
	
	public static synchronized boolean registerUser(String username, String pw) throws IOException{
		HashMap<String,String> registeredUsers = getRegisteredUsers();
		if (checkAvailability(registeredUsers, username)){
			addUserToFile(username, pw);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isUserLoggedIn(String username){
		if (loggedInUsers == null){loggedInUsers = new ArrayList<String>();}
		if (loggedInUsers.contains(username)){
			return true;
		} else {
			loggedInUsers.add(username);
			return false;
		}
	}
	
	public static boolean checkUserExist(String username) throws IOException{
		HashMap<String,String> registeredUsers = getRegisteredUsers();
		if (registeredUsers.containsKey(username)){
			return true;
		}
		return false;
	}
	
	public static boolean checkPW(String username, String pw) throws IOException{
		HashMap<String,String> registeredUsers = getRegisteredUsers();
		if (registeredUsers.get(username).equals(pw)){ //might have to cast to String
			return true;
		} else {
			return false;
		}
	}
	
	public static void changePW(String username, String oldpw, String newpw){
		
	}
	
	private static HashMap<String,String> getRegisteredUsers() throws IOException{
	    HashMap<String,String> creds = new HashMap<String,String>();
		try {
			File file = new File(System.getProperty("user.home") + "\\Documents\\git\\LockerRoom\\users.txt");
			//On server, would need to modify the directory
			BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	String[] lineArray= line.split(Pattern.quote("/"));
		    	creds.put(lineArray[0], lineArray[1]);	
		    }
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return creds;
	}
	
	private static boolean checkAvailability(HashMap<String,String> registered, String desiredName){
		boolean isAvailable = true;
		for (int i = 0; i < registered.size(); i++){
			if (registered.get(desiredName) != null){
				isAvailable = false;
			}
		}
		return isAvailable;
	}

	private static void addUserToFile(String username, String pw) throws IOException {
		try {
			File file = new File(System.getProperty("user.home") + "\\Documents\\git\\LockerRoom\\users.txt");
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(username + "/" + pw);
			bw.flush();
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("User registered");
	}
	
	

}
