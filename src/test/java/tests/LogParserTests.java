package tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import actions.LogParserActions;

public class LogParserTests {

	LogParserActions actions;
	
	@Test(description = "The first game has no kills. "
			+ "This test verifies that the total kills is set as 0")
	public void verifyZeroTotalKillsOnGameWithNoKills() {
		actions.verifyZeroTotalKillsOnGame1();
	}
	
	@Test(description = "This test loops through all the games in the log to "
			+ "assert that no players are duplicated on a game")
	public void verifyThereAreNoRepeatedPlayers() {
		actions.checkAllGamesForDuplicatedPlayers();
	};
	
	@Test(description = "This test checks if the total kills are being "
			+ "calculated correctly on all games")
	public void verifyTotalKillsCalculation() {
		actions.compareTotalKillsWithLogOnAllGames();
	}
	
	@Test(description = "This test gets the sum of kills on the Kills by Means "
			+ "dictionary and compares it to the total kills, to make sure "
			+ "they match")
	public void verifyKillsByMeansMatchesTotalKills() {
		actions.compareKillsByMeansWithTotalKillsOnAllGames();
	}
	
	@Test(description = "This test checks if the Player Ranking is being "
			+ "sorted by descending order of scores")
	public void verifyPlayerRankingIsOnDescendingOrder() {
		actions.checkIfRankingIsOnDescendingOrder();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		actions = new LogParserActions();
	}

}
