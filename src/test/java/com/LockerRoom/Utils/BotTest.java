package com.LockerRoom.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class BotTest {

	Bot bot;

	@Before
	public void runOnceBeforeClass(){
		bot = new Bot("Jimmy");
	}
	
	@Test
	public void rollDiceReturnsIntLessThanSixAndGreaterThan0() throws Exception{
		Method method = Bot.class.getDeclaredMethod("rollDice");
		method.setAccessible(true);
		int result = 0;
		for (int i = 0; i < 100; i++){
			result = (int) method.invoke(bot);
			assertTrue(result < 7);
			assertTrue(result > 0);
		}
	}
	
	@Test
	public void flipCoinReturnsHeadsOrTails() throws Exception{
		Method method = Bot.class.getDeclaredMethod("flipCoin");
		method.setAccessible(true);
		String result;
		int heads = 0;
		int tails = 0;
		for (int i = 0; i < 1000; i++){
			result = (String) method.invoke(bot);
			assertTrue(result.equals("Head") || result.equals("Tail"));
			if (result.equals("Head")) {
				heads++;
			} else {
				tails++;
			}
		}
		assertTrue(heads > 0);
		assertTrue(tails > 0);
	}
	
	@Test
	public void choosePicksSomethingFromListOfArgsAndReturnsOneOfThem() throws Exception{
		Method method = Bot.class.getDeclaredMethod("choose", String[].class);
		method.setAccessible(true);
		String result;
		String[] choices = 
			{"Bobby", "Billy", "Joel", "Tommy", "Michael", "Jon"};
		for (int i = 0; i < 1000; i++){
			result = (String) method.invoke(bot, (Object) choices);
			assertTrue(result.getClass().equals(String.class));
			assertTrue(result.equals(choices[0]) ||
						result.equals(choices[1]) ||
						result.equals(choices[2]) ||
						result.equals(choices[3]) ||
						result.equals(choices[4]) ||
						result.equals(choices[5]));
		}
	}
	
	@Test
	public void checkTimeReturnsTimeInCorrectFormat() throws Exception{
		Method method = Bot.class.getDeclaredMethod("checkTime");
		method.setAccessible(true);
		String result = (String) method.invoke(bot);
		assertTrue(Pattern.matches
				("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}", result));
	}
	
	@Test
	public void doCommandCallsChooseMethodProperly(){
		String result = bot.doCommand("!choose A B");
		result = result.split(": ")[1];
		assertTrue(result.equals("A") || result.equals("B"));
	}

}
