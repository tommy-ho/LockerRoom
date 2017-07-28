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
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


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
		if (LockerRoomCipher.decryptPW(registeredUsers.get(username)).equals(pw)){
			System.out.println("Password matched for " + username);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean changePW(String username, String oldpw, String newpw) throws IOException{
		if (!checkUserExist(username)){
			return false;
			}
		if (checkPW(username, oldpw)) {
			File file = new File("users.txt");
			File outputFile = new File("output.txt");
			FileWriter fw = new FileWriter(outputFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	if (!username.equals(line.split(Pattern.quote("///"))[0])){
		    		bw.write(line);
		    		bw.newLine();
		    	}
		    }
		    bw.flush();
		    bw.close();
			br.close();
			file.renameTo(new File("users-backup.txt"));
			outputFile.renameTo(new File("users.txt"));
			addUserToFile(username, newpw);
			return true;
		} else {
			return false;
		}
	}
	
	private static HashMap<String,String> getRegisteredUsers() throws IOException{
	    HashMap<String,String> creds = new HashMap<String,String>();
		try {
			File file = new File("users.txt");
			//File file = new File(System.getProperty("user.home") + "\\Documents\\git\\LockerRoom\\users.txt");
			//On server, would need to modify the directory
			BufferedReader br = new BufferedReader(new FileReader(file));
		    String line;
		    
		    while ((line = br.readLine()) != null) {
		    	String[] lineArray = line.split(Pattern.quote("///"));
		    	if (lineArray != null) {creds.put(lineArray[0], lineArray[1]);}	
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
			pw = LockerRoomCipher.encryptPW(pw);
			File file = new File("users.txt");
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(username + "///" + pw);
			bw.flush();
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
