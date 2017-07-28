package com.LockerRoom.Utils;

import java.util.Base64;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class LockerRoomCipher {
	
	static String encryptPW(String pw){
		byte[] pwBytes = pw.getBytes();
    	byte[] keyBytes = "0123456789abcdef".getBytes();
    	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    	String iv = null;
        try {
        	Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ciph.init(Cipher.ENCRYPT_MODE, keySpec);
            
            ////////////////////printing encrypted, pre-encoded bytes
        	for (byte b : ciph.doFinal(pwBytes)){
    			System.out.print(b);
    		}
    		System.out.println();
    		///////////////////////
    		
            pw = Base64.getEncoder().encodeToString(ciph.doFinal(pwBytes));
            iv = Base64.getEncoder().encodeToString(ciph.getIV());
            
		} catch (Exception e){
			e.printStackTrace();
		}
		return iv + "|||" + pw;
	}
	
	static String decryptPW(String pw){
    	String[] lineArray= pw.split(Pattern.quote("|||"));

		byte[] pwBytes = Base64.getDecoder().decode(lineArray[1].getBytes());
		
		/////////////////////printing decoded, still encrypted bytes
		for (byte b : pwBytes){
			System.out.print(b);
		}
		System.out.println();
		//////////////////////
		
		byte[] ivBytes = Base64.getDecoder().decode(lineArray[0].getBytes());
    	byte[] keyBytes = "0123456789abcdef".getBytes();
    	SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        try {
			Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ciph.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
            pw = new String(ciph.doFinal(pwBytes));    
		} catch (Exception e){
			e.printStackTrace();
		}
		return pw;
	}

}
