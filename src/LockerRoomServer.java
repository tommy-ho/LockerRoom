import java.io.*;
import java.net.*;
import java.util.*;

public class LockerRoomServer {
	
	private static LockerRoomServer mrs = null;
	private ArrayList<Writer> array; //client list

	public static void main(String[] args) {
		mrs = getInstance();
		mrs.go();

	}
	
	protected LockerRoomServer() {
		//Protected to prevent instantiation from outside
	}
	
	public static LockerRoomServer getInstance() {
		if (mrs == null){
			mrs = new LockerRoomServer();
		}
		return mrs;
	}
	
	public void go(){
		array = new ArrayList<Writer>();
		try {
			ServerSocket serverSocket = new ServerSocket(5000);
			while (true){
				Socket clientSocket = serverSocket.accept();
				PrintWriter writer = 
						new PrintWriter(clientSocket.getOutputStream());
				array.add(writer);
				
				Thread t = new Thread(new ClientManager(clientSocket));
				t.start();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void publishMessage(String message){
		
		Iterator<Writer> it = array.iterator();
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
	
	//Inner class
	public class ClientManager implements Runnable {
		
		private Socket clientSock;
		private BufferedReader reader;
		
		public ClientManager(Socket sock){
			try {
				clientSock = sock;
				InputStreamReader isReader = 
						new InputStreamReader(clientSock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			String message;
			try { //Reads from this client
				while ((message = reader.readLine()) != null){
					System.out.print("Read: " + message);
					//Then tells everyone else in the arraylist
					publishMessage(message);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
	} //end of inner class

}
