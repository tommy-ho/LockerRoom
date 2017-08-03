package com.LockerRoom.Client;

import java.util.ArrayList;

/**
 * This is an ArrayList class meant to store LockerRoomClient objects.
 * The purpose of it is to implement a thread-safe add method to the
 * ArrayList.
 * 
* @author  Tommy Ho
* */
public class ClientArrayList<E> extends ArrayList<E>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Utilizes synchronized keyword is ensure a thread-safe way of
	 * adding LockerRoomClient objects to a list.
	 *
	 * @param
	 * @return
	 * @see ArrayList#add(Object)
	 */
	public synchronized void threadSafeAdd(E e){
		super.add(e);
	}
	
}