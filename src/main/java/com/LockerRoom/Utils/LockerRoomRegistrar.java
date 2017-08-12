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
import java.nio.file.Files;
import java.nio.file.Path;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is in charge of user registration, login verification, and password
 * updates. The utility methods are public static for outside access.
 * 
* @author  Tommy Ho
* */
public class LockerRoomRegistrar {
	
	private static ArrayList<String> loggedInUsers;
	
	/**
	 * This registerUser() method takes in 2 String arguments (username and password)
	 * and checks it against the list of users for availability. If available,
	 * it will add the user's desired credentials to the list. It is also synchronized
	 * so no two user can register at once.
	 *
	 * @param username the desired username entered in the online JSP form
	 * @param pw the desired password entered in the online JSP form
	 * @return boolean representing success of registering the user
	 * @see #checkUserExist(String)
	 * @see #addUserToFile(String, String)
	 */
	public static synchronized boolean registerUser(String username, String pw) throws IOException{
//		HashMap<String,String> registeredUsers = getRegisteredUsers();
//		if (checkAvailability(registeredUsers, username)){
//			addUserToFile(username, pw);
//			return true;
//		} else {
//			return false;
//		}
		
		if (!checkUserExist(username)){
			addUserToFile(username, pw);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This isUserLoggedIn() method takes an String input of username and checks
	 * a static ArrayList to see if the user is logged in. It returns true if the
	 * user is logged in, otherwise it will return false and add the user to the list.
	 *
	 * @param username the username entered in the online JSP form
	 * @return boolean representing if the user is already logged in
	 * @see
	 */
	public static boolean isUserLoggedIn(String username){
		if (loggedInUsers == null){loggedInUsers = new ArrayList<String>();}
		if (loggedInUsers.contains(username)){
			return true;
		} else {
			loggedInUsers.add(username);
			return false;
		}
	}
	
	/**
	 * This checkUserExist() method takes an String input of username and checks
	 * it against a HashMap of all LockerRoom users to see if the username exist.
	 * It returns true if the username already exists in the system, otherwise it
	 * will return false.
	 *
	 * @param username the username entered in the online JSP form
	 * @return boolean representing if the username is already taken
	 * @see
	 */
	public static boolean checkUserExist(String username) throws IOException{
		HashMap<String,String> registeredUsers = getRegisteredUsers();
		if (registeredUsers.containsKey(username)){
			return true;
		}
		return false;
	}
	
//	private static boolean checkAvailability(HashMap<String,String> registered, String desiredName){
//	boolean isAvailable = true;
//	for (int i = 0; i < registered.size(); i++){
//		if (registered.get(desiredName) != null){
//			isAvailable = false;
//		}
//	}
//	return isAvailable;
//}
	
	/**
	 * This checkPW() method takes an String inputs of username and password
	 * and checks it against a HashMap of all LockerRoom user credentials.
	 * It returns true if the password matches, and false otherwise.
	 *
	 * @param username the username entered in the online JSP form
	 * @return boolean representing if the password for the username is correct
	 * @see #getRegisteredUsers()
	 * @see LockerRoomCipher#decryptPW(String)
	 */
	public static boolean checkPW(String username, String pw) throws IOException{
		HashMap<String,String> registeredUsers = getRegisteredUsers();
		if (LockerRoomCipher.decryptPW(registeredUsers.get(username)).equals(pw)){
			System.out.println("Password matched for " + username);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * The changePW() method does exactly what you think it does. It takes
	 * 3 inputs from the user (username, old password, and new password) and
	 * changes the user's credentials stored after verification.
	 *
	 * @param username the username entered in the online JSP form
	 * @param oldpw the current password that matches that username
	 * @param newpw the new password the user wants to change to
	 * @return boolean representing if the password was changed successfully
	 * @see #checkUserExist(String)
	 * @see #checkPW(String, String)
	 * @see #addUserToFile(String, String)
	 */
	public static boolean changePW(String username, String oldpw, String newpw) throws IOException{
		if (!checkUserExist(username) || newpw.equals("")) {
			return false;
		}
		
		if (checkPW(username, oldpw)) {
			File file = new File("users.txt");
			File backupFile = new File("users-backup.txt");
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
			backupFile.delete();
			file.renameTo(backupFile);
			outputFile.renameTo(file);
			addUserToFile(username, newpw);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * getRegisteredUsers() is a private utility method that parses the user's login
	 * data and returns a HashMap object containing all registered user information.
	 *
	 * @param
	 * @return HashMap with user login data still encrypted
	 * @see
	 */
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

	/**
	 * addUserToFile() is a private utility method that adds the user's information
	 * to the registered users file. This method calls upon the LockerRoomCipher
	 * utility to encrypt the password.
	 *
	 * @param username the desired username to add to the file
	 * @param pw the corresponding password to that username
	 * @return 
	 * @see LockerRoomCipher#encryptPW(String)
	 */
	private static void addUserToFile(String username, String pw) throws IOException {
		try {
			pw = LockerRoomCipher.encryptPW(pw);
			File file = new File("users.txt");
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(username + "///" + pw);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean removeUser(String username, String pw) throws IOException {
		if (!checkUserExist(username)){
			return false;
			}
		if (checkPW(username, pw)) {
			File file = new File("users.txt");
			File backupFile = new File("users-backup.txt");
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
			backupFile.delete();
			file.renameTo(backupFile);
			outputFile.renameTo(file);
			return true;
		} else {
			return false;
		}
	}
}
