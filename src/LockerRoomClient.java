import java.io.*;
import java.net.*;
import java.util.*;

public class LockerRoomClient implements Runnable {

	private static LockerRoomClient mrc = null;
	private Socket socket;
	private PrintWriter writer;
	private Scanner sc;
	//use for testing purposes right now for getting message
	//replace with web in future
	
	public static void main(String[] args) {
		new Thread(LockerRoomClient.getInstance()).start();
		//pretty sure this is all that goes here
	}
	
	protected LockerRoomClient() {
		//Protected to prevent instantiation from outside
	}
	
	public static LockerRoomClient getInstance(){ //Singleton design pattern
		if (mrc == null){
			mrc = new LockerRoomClient();
		}
		return mrc;
	}

	@Override
	public void run() {
		connectToNetwork();
		sendMessage(getMessage());
		
	}
	
	public void connectToNetwork(){
		try {
			System.out.println("Testing");
			socket = new Socket("192.168.1.175", 5000); //Connect to my Linux server
			System.out.println("Testing"); //doesnt get here yet
			writer = new PrintWriter(socket.getOutputStream());
			//Set writer's output to the socket's output stream
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public String getMessage(){  //temp class for testing
		sc = new Scanner(System.in);
		System.out.println("Enter msg:");
		String message = sc.nextLine();
		return message;
	}
	
	public void sendMessage(String message){ //temp class for testing
		writer.println(message);
		writer.flush();
	}
	
	
	
	//inner class listener to listen to server responses

}
