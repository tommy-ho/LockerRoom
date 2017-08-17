package com.LockerRoom.Client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LockerRoomClientTest {
	
	static LockerRoomClient lrc;
	static String n;

	@BeforeClass
	public static void runOnceBeforeClass(){
		n = Long.toHexString(Double.doubleToLongBits(Math.random()));
		lrc = new LockerRoomClient(n);
	}
	
	@Test
	public void TestA_instantiatingLockerRoomClientCreateslrcListObject(){
		assertTrue(LockerRoomClient.getLrcList() != null);
		assertEquals(1, LockerRoomClient.getLrcList().size());
	}
	
	@Test
	public void TestB_newLockerRoomClientIncreaseslrcListObject(){
		assertEquals(1, LockerRoomClient.getLrcList().size());
		new LockerRoomClient(n + "2");
		assertEquals(2, LockerRoomClient.getLrcList().size());
	}
	
	@AfterClass
	public static void runOnceAfterClass(){

	}

}
