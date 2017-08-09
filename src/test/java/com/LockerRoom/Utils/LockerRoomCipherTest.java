package com.LockerRoom.Utils;

import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import org.junit.*;

public class LockerRoomCipherTest {

	@Before
	public void runOnceBeforeClass(){
	}
	
	@Test
	public void encryptPWReturnsStringWithThreePartsAndTwoDelimiters(){
		String pw = "testPW123";
		String encryptedPW = LockerRoomCipher.encryptPW(pw);
		int delimitedParts = encryptedPW.split(Pattern.quote("|||")).length;
		assertEquals(3, delimitedParts);
		assertTrue(delimitedParts == 3);
	}
	
	@Test
	public void decryptPWReturnsSameStringPassedToEncryptPWMethod(){
		String encryptedPW = LockerRoomCipher.encryptPW("testing123");
		String decryptedPW = LockerRoomCipher.decryptPW(encryptedPW);
		assertEquals("testing123", decryptedPW);
	}
	
	@Test
	public void getKeyBytesReturnsByteArrayOfLength16(){
		try {
			Method method = LockerRoomCipher.class.getDeclaredMethod("getKeyBytes");
			method.setAccessible(true);
			byte[] keyBytes = (byte[]) method.invoke(new LockerRoomCipher());
			assertEquals(16, keyBytes.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
