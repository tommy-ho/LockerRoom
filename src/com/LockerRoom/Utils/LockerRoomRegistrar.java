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
			System.out.println(LockerRoomCipher.decryptPW(registeredUsers.get(username)) + " : password matched!");
			return true;
		} else {
			return false;
		}
	}
	
	public static void changePW(String username, String oldpw, String newpw) throws IOException{
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
		    	String[] lineArray= line.split(Pattern.quote("///"));
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
			pw = LockerRoomCipher.encryptPW(pw);
			File file = new File("users.txt");
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.newLine();
			bw.write(username + "/" + pw);
			bw.flush();
			bw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
//	private static String encryptPW(String pw){
//		byte[] pwBytes = pw.getBytes();
//    	byte[] keyBytes = "0123456789abcdef".getBytes();
//    	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
//    	String iv = null;
//        try {
//        	Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            ciph.init(Cipher.ENCRYPT_MODE, keySpec);
//            pw = Base64.getEncoder().encodeToString(ciph.doFinal(pwBytes));
//            iv = Base64.getEncoder().encodeToString(ciph.getIV());
//            
//    		for (byte b : pwBytes){
//    			System.out.print(b);
//    		}
//    		System.out.println();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return iv + "|" + pw;
//	}
//	
//	private static String decryptPW(String pw){
//    	String[] lineArray= pw.split(Pattern.quote("|||"));
//
//		byte[] pwBytes = Base64.getDecoder().decode(lineArray[1].getBytes());
//		for (byte b : pwBytes){
//			System.out.print(b);
//		}
//		System.out.println();
//		
//		byte[] ivBytes = Base64.getDecoder().decode(lineArray[0].getBytes());
//    	byte[] keyBytes = "0123456789abcdef".getBytes();
//    	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
//        try {
//			Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            ciph.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
//            pw = new String(ciph.doFinal(pwBytes));    
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		return pw;
//	}
	
	

}
