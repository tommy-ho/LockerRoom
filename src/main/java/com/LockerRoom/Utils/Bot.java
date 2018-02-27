package com.LockerRoom.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import com.LockerRoom.Client.LockerRoomClient;

/**
	 * Bot extends LockerRoomClient and mimics all behaviors of
	 * being a real client, except it is entirely controlled by code
	 * and reacts to messages and commands given by users.
	 * 
 * @author  Tommy Ho
 * */
public class Bot extends LockerRoomClient implements Runnable {
	
	public Bot(String name) {
		super(name);
	}
	
	public void run() {
		super.connectToNetwork();
		getMessageFromServer();
	}
	
	protected void getMessageFromServer(){
		String message;
		
		try {
			while ((message = reader.readLine()) != null){
				if (message.matches("^.*: ![^\\s]+.*$")){
					String[] unameSplitCmd = message.split(": ");
					//Avoid interpreting bot messages as commands
					if (!unameSplitCmd[0].equals(username)){
						sendMessageToServer(this, doCommand(unameSplitCmd[1]));
						//Modify the command to record only commands
					}
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public String doCommand(String command){
		if (command.equals("!roll")){
			return "You have rolled a: " + rollDice();
		} else if (command.equals("!flip")){
			return "Your coin flipped: " + flipCoin();
		} else if (command.matches("!choose .+$")){
			System.out.println("test");
			String[] choice = command.split(" ");
			return "I choose: " + choose(choice);
		} else if (command.equals("!time")){
			return "System time: " + checkTime();
		}
		return command;
	}
	
	private int rollDice(){
		Random random = new Random();
		return random.nextInt(6) + 1;
	}
	
	private String flipCoin(){
		Random random = new Random();
		if (random.nextInt(2) == 0){
			return "Head";
		} else {
			return "Tail";
		}
	}
	
	private String choose(String[] args){
		Random random = new Random();
		int randomNumber = random.nextInt(args.length);
		if (randomNumber == 0) { randomNumber++; }
		return args[randomNumber];
	}
	
	private String checkTime(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return dateFormat.format(System.currentTimeMillis());
	}

}
