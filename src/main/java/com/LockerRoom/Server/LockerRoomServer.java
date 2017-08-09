package com.LockerRoom.Server;
import java.io.*;
import java.net.*;
import java.util.*;

	/**
 	 * This Runnable class consists of the LockerRoom code that actively listens for
 	 * new LockerRoomClient objects, and manages existing clients in a list. The subclass
 	 * ClientManager, also implementing Runnable, will focus on monitoring activity
 	 * from existing clients and distributing the messages.
 	 * 
     * @author  Tommy Ho
     * */
public class LockerRoomServer implements Runnable{
	
	private static LockerRoomServer lrs = null;
	private ArrayList<Writer> clientWriterArrayList; //client list
	
	private LockerRoomServer() {
		//Private to prevent instantiation from outside
	}
	
	/**
	 * The run() method here overrides the method inherited from the Runnable interface.
	 * It calls the class' go() method to get things rolling.
	 *
	 * @param
	 * @return
	 * @see #go()
	 * @see #getInstance()
	 */
	@Override
	public void run() {
//		lrs = getInstance();
//		lrs.go();
		go();
	}
	
	/**
	 * The getInstance() method returns the static LockerRoomServer instance.
	 * If it is null, it will instantiate it.
	 *
	 * @param
	 * @return the LockerRoomServer static instance
	 * @see #go()
	 */
	public static LockerRoomServer getInstance() {
		if (lrs == null){
			lrs = new LockerRoomServer();
		}
		return lrs;
	}
	
	/**
	 * The go() method is the core of this class. It opens up a new ServerSocket
	 * and listens for all incoming traffic from LockerRoomClient code. It accepts
	 * new clients and assigns a PrintWriter object to the client socket for sending
	 * future messages. Lastly, it creates a new thread with the ClientManager class
	 * to listen for messages from the client.
	 *
	 * @param
	 * @return
	 * @see
	 */
	private void go(){
		System.out.println("Server code started running");
		clientWriterArrayList = new ArrayList<Writer>();
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			while (true){
				Socket clientSocket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientWriterArrayList.add(writer);
				Thread t = new Thread(new ClientManager(clientSocket));
				t.start();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * When called upon by individual ClientManager threads, the publishMessage() method
	 * accesses the ArrayList of PrintWriter objects and flushes the message via socket
	 * output stream over to the LockerRoomClient objects to process.
	 *
	 * @param message a message retrieved from a ClientManager's InputStreamReader
	 * @return
	 * @see ClientManager#run()
	 */
	private void publishMessage(String message){
		Iterator<Writer> it = clientWriterArrayList.iterator();
		while (it.hasNext()){
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
 	 * This inner class is initiated as a new thread and read input from the clients
 	 * via an InputStreamReader.
 	 * 
     * @author  Tommy Ho
     * */
	private class ClientManager implements Runnable {
		
		private Socket clientSock;
		private BufferedReader reader;
		
		/**
		 * This constructor takes a socket and generates an InputStreamReader/BufferedReader
		 * for the ClientManager object.
		 *
		 * @param sock a socket for communication between the server and this particular client
		 * @return
		 * @see
		 */
		private ClientManager(Socket sock){
			try {
				clientSock = sock;
				InputStreamReader isReader = new InputStreamReader(clientSock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		/**
		 * The run() method here overrides the method inherited from the Runnable interface.
		 * Starting a new thread, it reads from the client via the ClientManager's InputStreamReader
		 * and passes the message to the outer class LockerRoomServer to handle.
		 *
		 * @param
		 * @return
		 * @see #publishMessage()
		 */
		@Override
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null){
					publishMessage(message);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
	} //end of inner class


}