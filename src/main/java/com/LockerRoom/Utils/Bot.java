package com.LockerRoom.Utils;

import java.util.Random;
import java.util.regex.Pattern;

public class Bot {
	
	String botName;

	public Bot(String name) {
		botName = name;
	}
	
	public String doCommand(String command){
		if (command.equals("!roll")){
			return "You have rolled a: " + rollDice();
		} else if (command.equals("!flip")){
			return "Your coin flipped: " + flipCoin();
		} else if (command.equals(Pattern.quote("!choose"))){
			String[] choice = command.split(" ");
			return choose(choice);
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

}
