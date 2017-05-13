package com.LockerRoom.Client;

import java.util.ArrayList;

public class ClientArrayList<E> extends ArrayList<E>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public synchronized void threadSafeAdd(E e){
		super.add(e);
	}
	
}