package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import actions.LogParserActions;

public class LogParserTests {

	LogParserActions actions;
    private static final Logger logger = LogManager.getLogger(LogParserTests.class);
	
	@Test(description = "The first game has no kills. "
			+ "This test verifies that the total kills is set as 0")
	public void verifyZeroTotalKillsOnGameWithNoKills() {
		logger.info("Starting test verifyZeroTotalKillsOnGameWithNoKills()");
		actions.verifyZeroTotalKillsOnGame1();
	}
	
	@Test(description = "This test loops through all the games in the log to "
			+ "assert that no players are duplicated on a game")
	public void verifyThereAreNoRepeatedPlayers() {
		logger.info("Starting test verifyThereAreNoRepeatedPlayers()");
		actions.checkAllGamesForDuplicatedPlayers();
	};
	
	@Test(description = "This test checks if the total kills are being "
			+ "calculated correctly on all games")
	public void verifyTotalKillsCalculation() {
		logger.info("Starting test verifyTotalKillsCalculation()");
		actions.compareTotalKillsWithLogOnAllGames();
	}
	
	@Test(description = "This test gets the sum of kills on the Kills by Means "
			+ "dictionary and compares it to the total kills, to make sure "
			+ "they match")
	public void verifyKillsByMeansMatchesTotalKills() {
		logger.info("Starting test verifyKillsByMeansMatchesTotalKills()");
		actions.compareKillsByMeansWithTotalKillsOnAllGames();
	}
	
	@Test(description = "This test checks if the Player Ranking is being "
			+ "sorted by descending order of scores")
	public void verifyPlayerRankingIsOnDescendingOrder() {
		logger.info("Starting test verifyPlayerRankingIsOnDescendingOrder()");
		actions.checkIfRankingIsOnDescendingOrder();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		actions = new LogParserActions(logger);
	}

	@AfterMethod
	public void afterMethod() {
		logger.info("------------------------------");
	}
}
