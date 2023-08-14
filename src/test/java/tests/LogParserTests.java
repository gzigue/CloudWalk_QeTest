package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import actions.LogParserActions;

public class LogParserTests {

	LogParserActions actions;
	
	@Test
	public void gameWithNoKills() {
		actions.verifyThereAreNoKillsOnGame1();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		actions = new LogParserActions();
	}

}
