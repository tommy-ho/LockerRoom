package com.LockerRoom.Utils;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LockerRoomRegistrarTest {
	
	static String testUser;
	static String testUser2;
	static String testUserDoesNotExist;
	static String testPW;

	@BeforeClass
	public static void runOnceBeforeClass(){
		testUser = Long.toHexString(Double.doubleToLongBits(Math.random()));
		testUser2 = Long.toHexString(Double.doubleToLongBits(Math.random()));
		testUserDoesNotExist = Long.toHexString(Double.doubleToLongBits(Math.random()));
		testPW = "testpw1";
	}
	
	@Test
	public void TestA_checkUserExistReturnsFalseIfUserDoesNotExists(){
		try {
			assertFalse(LockerRoomRegistrar.checkUserExist(testUser));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestB_addUserToFileAddsUserToTheFile(){
		try {
			Method method = LockerRoomRegistrar.class.getDeclaredMethod
					("addUserToFile", String.class, String.class);
			method.setAccessible(true);
			method.invoke(new LockerRoomRegistrar(), testUser, testPW);
			assertTrue(LockerRoomRegistrar.checkUserExist(testUser));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestC_checkUserExistReturnsTrueIfUserExists(){
		try {
			assertTrue(LockerRoomRegistrar.checkUserExist(testUser));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestD_registerUserReturnsFalseIfUserExists(){
		try {
			assertFalse(LockerRoomRegistrar.registerUser(testUser, testPW));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestD_registerUserReturnsTrueIfUserDoesNotExist(){
		try {
			assertTrue(LockerRoomRegistrar.registerUser(testUser2, testPW));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestE_isUserLoggedInReturnsFalseForUserNotLoggedInThenAddToList(){
		assertFalse(LockerRoomRegistrar.isUserLoggedIn(testUser));
		assertTrue(LockerRoomRegistrar.isUserLoggedIn(testUser));
	}
	
	@Test
	public void TestF_checkPWReturnsFalseIfPasswordIsWrong(){
		try {
			assertFalse(LockerRoomRegistrar.checkPW(testUser, testPW + "12345"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestG_checkPWReturnsTrueIfPasswordIsCorrect(){
		try {
			assertTrue(LockerRoomRegistrar.checkPW(testUser, testPW));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestH_changePWReturnsFalseIfOldPWDoesNotMatch(){
		try {
			assertFalse(LockerRoomRegistrar.changePW
					(testUser, testPW + "123", testPW + "new"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestH_changePWReturnsFalseIfUserDoesNotExist(){
		try {
			assertFalse(LockerRoomRegistrar.changePW
					(testUserDoesNotExist, testPW, testPW + "new"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestI_changePWReturnsFalseIfNewPWIsBlank(){
		try {
			assertFalse(LockerRoomRegistrar.changePW
					(testUser, testPW, ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestJ_getRegisteredUsersReturnsHashMapInProperFormat(){
		try {
			Method method = LockerRoomRegistrar.class.getDeclaredMethod
					("getRegisteredUsers");
			method.setAccessible(true);
			@SuppressWarnings("unchecked")
			HashMap<String,String> results = 
					(HashMap<String, String>) method.invoke(new LockerRoomRegistrar());
			assertTrue(results.getClass().equals(HashMap.class));
			assertTrue(results.containsKey(testUser));
			assertFalse(results.containsKey(testUserDoesNotExist));
			assertTrue(results.get(testUser).getClass().equals(String.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void TestK_addUserToFileAddsUserToTxtFile(){
		try {
			Method method = LockerRoomRegistrar.class.getDeclaredMethod
					("addUserToFile", String.class, String.class);
			Method method2 = LockerRoomRegistrar.class.getDeclaredMethod
					("getRegisteredUsers");
			method.setAccessible(true);
			method2.setAccessible(true);
			HashMap<String,String> results = 
					(HashMap<String, String>) method2.invoke(new LockerRoomRegistrar());
			assertFalse(results.containsKey(testUserDoesNotExist));
			method.invoke(new LockerRoomRegistrar(), testUserDoesNotExist, testPW);
			results = (HashMap<String, String>) method2.invoke(new LockerRoomRegistrar());
			assertTrue(results.containsKey(testUserDoesNotExist));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void TestL_removeUserRemovesUserFromTxtFile(){
		try {
			Method method = LockerRoomRegistrar.class.getDeclaredMethod
					("removeUser", String.class, String.class);
			Method method2 = LockerRoomRegistrar.class.getDeclaredMethod
					("getRegisteredUsers");
			method.setAccessible(true);
			method2.setAccessible(true);
			HashMap<String,String> results = 
					(HashMap<String, String>) method2.invoke(new LockerRoomRegistrar());
			assertTrue(results.containsKey(testUserDoesNotExist));
			method.invoke(new LockerRoomRegistrar(), testUserDoesNotExist, testPW);
			results = (HashMap<String, String>) method2.invoke(new LockerRoomRegistrar());
			assertFalse(results.containsKey(testUserDoesNotExist));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void runOnceAfterClass(){
		try {
			LockerRoomRegistrar.removeUser(testUser, testPW);
			LockerRoomRegistrar.removeUser(testUser2, testPW);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
