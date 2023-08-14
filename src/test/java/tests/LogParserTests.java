package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import actions.LogParserActions;

public class LogParserTests {

	LogParserActions actions;
	
	@Test
	public void verifyZeroTotalKillsOnGameWithNoKills() {
		actions.verifyZeroTotalKillsOnGame1();
	}
	
	@Test
	public void verifyThereAreNoRepeatedPlayers() {
		actions.checkAllGamesForDuplicatedPlayers();
	};
	
	@Test
	public void verifyTotalKillsCalculation() {
		actions.compareTotalKillsWithLogOnAllGames();
	}
	
	@Test
	public void verifyKillsByMeansMatchesTotalKills() {
		actions.compareKillsByMeansWithTotalKillsOnAllGames();
	}
	
	@Test
	public void verifyPlayerRankingIsOnDescendingOrder() {
		actions.checkIfRankingIsOnDescendingOrder();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		actions = new LogParserActions();
	}

}
