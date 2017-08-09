package com.LockerRoom.Utils;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import org.junit.*;

public class LockerRoomRegistrarTest {
	
	String testUser;
	String testPW;

	@Before
	public void runOnceBeforeClass(){
		testUser = "testuser1";
		testPW = "testpw1";
		try {
			if (LockerRoomRegistrar.checkUserExist(testUser)){
				LockerRoomRegistrar.removeUser(testUser, testPW);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void addUserToFileAddsUserToTheFile(){
		try {
			Method method = LockerRoomRegistrar.class.getDeclaredMethod
					("addUserToFile", String.class, String.class);
			method.setAccessible(true);
			method.invoke(new LockerRoomRegistrar(), "testuser1", "testpw1");
			assertTrue(LockerRoomRegistrar.checkUserExist("testuser1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void runOnceAfterClass(){
		try {
			LockerRoomRegistrar.removeUser(testUser, testPW);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
