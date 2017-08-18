package com.LockerRoom.Client;
import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.LockerRoom.Utils.LockerRoomRegistrar;

/**
	 * The LockerRoomClient class implements Runnable, and is meant to be started as a
	 * thread. Upon starting the thread, it connects to the server side code, and
	 * starts listening for messages from other clients and queuing them in a buffer.
	 * 
 * @author  Tommy Ho
 * */
public class LockerRoomClient implements Runnable {

	private String username;
	private Socket socket;
	private PrintWriter writer;
	private InputStreamReader isReader;
	private BufferedReader reader;
	private ArrayList<String> messageBuffer;
	private static ClientArrayList<LockerRoomClient> lrcList;

	/**
	 * This constructor takes a String variable (representing the username) as a parameter
	 * to instantiate a LockerRoomClient object. It then adds the client to a list of
	 * active users for tracking and reference purposes.
	 *
	 * @param n a String representing the username associated with this LockerRoomClient
	 * @return
	 * @see
	 */
	public LockerRoomClient(String n) {
		this.setUsername(n);
		if (lrcList == null) lrcList = new ClientArrayList<LockerRoomClient>();
		lrcList.threadSafeAdd(this);
		System.out.println(username + " : Client get initiated");
	}

	/**
	 * Overrides the Runnable interface's run() method. Runs the connectToNetwork() method
	 * to try connecting to server. Then runs getMessageFromServer() to listen to all
	 * incoming messages from server.
	 *
	 * @param
	 * @return
	 * @see #connectToNetwork()
	 * @see #getMessageFromServer()
	 */
	@Override
	public void run() {
		connectToNetwork();
		getMessageFromServer();
	}
	
	/**
	 * This method creates a socket connection to the server side part of the code.
	 * It also initializes the LockerRoomClient's private members to be used.
	 *
	 * @param
	 * @return
	 * @see
	 */
	private void connectToNetwork(){
		try {
			socket = new Socket("localhost", 5000);
			//Connect to my Linux server for chat functionality 192.168.1.175
			//or localhost Tomcat server during testing
			System.out.println("Client is connected to server");
			writer = new PrintWriter(socket.getOutputStream());
			isReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isReader);
			messageBuffer = new ArrayList<String>();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Through this method, the LockerRoomClient (and its thread) will continually
	 * listen to the server for broadcasted messages and store it in an array for
	 * the user to access when refreshing their pages.
	 *
	 * @param
	 * @return
	 * @see
	 */
	private void getMessageFromServer(){
		String message;
		
		try {
			while ((message = reader.readLine()) != null){
				messageBuffer.add(message);
				if (messageBuffer.size() > 100){
					while (messageBuffer.size() > 80) messageBuffer.remove(0);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	/**
	 * The getMessageFromServlet() method is a public method for the servlet's
	 * doPost() method to call. This method will then call on the sendMessageToServer()
	 * method, passing on this LockerRoomClient and the message as the arguments.
	 *
	 * @param message the text submitted by the user
	 * @return
	 * @see #sendMessageToServer(LockerRoomClient, String)
	 */
	public void getMessageFromServlet(String message){
		sendMessageToServer(this, message);
	}
	
	/**
	 * This is a private method only called on by the getMessageFromServlet() method.
	 * The method takes the message and utilizes the PrintWriter that is connected
	 * to the output stream to submit messages to the server side code.
	 *
	 * @param message the text submitted by the user
	 * @return
	 * @see #sendMessageToServer(LockerRoomClient, String)
	 */
	private void sendMessageToServer(LockerRoomClient client, String message){ 
		writer.println(client.username + ": " + message);
		writer.flush();
	}
	
	//Not Used
	public void sendMessageToServlet(){
		//This is the method that refreshes user JSP periodically? per message? and prompt refresh
		String url = "http://localhost:8080/LockerRoom/ChatServlet";
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		//HttpGet get = new HttpGet(url);
		
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("message", new String("")));
		postParameters.add(new BasicNameValuePair("username", this.getUsername()));

		try {
			post.setEntity(new UrlEncodedFormEntity(postParameters));
			HttpResponse response = httpClient.execute(post);
			//HttpResponse response = httpClient.execute(get);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disconnectFromServer() {
		String disconnectMessage = "!disconnect";
		lrcList.remove(this);
		LockerRoomRegistrar.modifyLoggedInUsersList("remove", this.username);
		sendMessageToServer(this, disconnectMessage);
		return;
	}
	
	public ArrayList<String> getMessageBuffer(){
			return messageBuffer;
	}
	
	public static ClientArrayList<LockerRoomClient> getLrcList() {
		return lrcList;
	}

	public static void setLrcList(ClientArrayList<LockerRoomClient> lrcList) {
		LockerRoomClient.lrcList = lrcList;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
