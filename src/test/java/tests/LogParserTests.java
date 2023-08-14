package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import actions.LogParserActions;

public class LogParserTests {

	LogParserActions actions;
	
	@Test
	public void verifyZeroTotalKillsOnGameWithNoKills() {
		actions.verifyThereAreNoKillsOnGame1();
	}
	
	@Test
	public void verifyThereAreNoRepeatedPlayers() {};
	
	@Test
	public void verifyTotalKillsCalculation() {}
	
	@Test
	public void verifyKillsByMeansMatchesTotalKills() {}
	
	@Test
	public void verifyPlayerRankingMatchesScoreSums() {}
	
	@Test
	public void verifyPlayerRankingIsOnDescendingOrder() {}
	
	@BeforeMethod
	public void beforeMethod() {
		actions = new LogParserActions();
	}

}
