package com.LockerRoom.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.regex.Pattern;

import com.LockerRoom.Client.LockerRoomClient;

/**
	 * Bot extends LockerRoomClient and mimics all behaviors of
	 * being a real client, except it is entirely controlled by code
	 * and reacts to messages and commands given by users.
	 * 
 * @author  Tommy Ho
 * */
public class Bot extends LockerRoomClient{
	
	private String botName;

	public Bot(String name) {
		super(name);
		botName = super.getUsername();
	}
	
	public String doCommand(String command){
		if (command.equals("!roll")){
			return "You have rolled a: " + rollDice();
		} else if (command.equals("!flip")){
			return "Your coin flipped: " + flipCoin();
		} else if (command.equals(Pattern.quote("!choose"))){
			String[] choice = command.split(" ");
			return choose(choice);
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
		return args[random.nextInt(args.length)];
	}
	
	private String checkTime(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		return dateFormat.format(System.currentTimeMillis());
	}

}
