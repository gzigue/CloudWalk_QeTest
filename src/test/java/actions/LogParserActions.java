package actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.quakelogparser.Game;
import com.quakelogparser.LogParser;

public class LogParserActions extends LogParser{
	
	private String filePath = "src\\main\\resources\\qgames.log";
	private LinkedHashMap<String, Game> gameStats;
    private Logger logger;
	
	public LogParserActions(Logger logger) {
		this.logger = logger;
	}
	
	public LinkedHashMap<String, Game> getGameStatsFromLogFile() {
		String relevantLogs = filterLog(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);

		logger.info("Getting the games' stats from the log.");
		return calculateGameStats(listOfGames);
	}
	
	public int getTotalKillsFromGame1() {
		gameStats = getGameStatsFromLogFile();
		logger.info("Getting the total number of kills on Game 1");
		int totalKills = gameStats.get("game_1").getTotal_kills();
		logger.info("Total number of kills on Game 1: " + totalKills);
		return totalKills;
	}
	
	public void verifyZeroTotalKillsOnGame1() {
		int totalKills = getTotalKillsFromGame1();
		Assert.assertEquals(totalKills, 0, 
				"The total number of kills on Game 1 should be 0, but is actually " +
				getTotalKillsFromGame1());
	}
	
	public void checkAllGamesForDuplicatedPlayers() {
		gameStats = getGameStatsFromLogFile();
		Set<String> statsSet = gameStats.keySet();
		
		logger.info("Looping through all the games to search for duplicate players");
		for(String gameName : statsSet) {
			Game game = gameStats.get(gameName);
			ArrayList<String> players = game.getPlayers();
			
			Set<String> uniquePlayers = new HashSet<String>(players);
			Assert.assertEquals(players.size(), uniquePlayers.size(),
					"There is a duplicate player in " + gameName);
			logger.info(gameName + ": No duplicate players");
		}
	}
	
	public void compareTotalKillsWithLogOnAllGames() {
		String relevantLogs = filterLog(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);
		
		gameStats = calculateGameStats(listOfGames);
		
		int index = 1;
		logger.info("Looping through all games tocompare the number of kills with the log");
		for(String game : listOfGames) {
			int numberOfKills = game.split("Kill:").length-1;
			int totalKills = gameStats.get("game_"+index).getTotal_kills();
			logger.info("game_" + index + 
					" - Kills on the log: " + numberOfKills + 
					" - total_kills: " + totalKills);
			
			Assert.assertEquals(numberOfKills, totalKills,
					"The total kills differs from log in game_"+index);
			index++;
		}
	}
	
	public void compareKillsByMeansWithTotalKillsOnAllGames() {
		gameStats = getGameStatsFromLogFile();
		Set<String> statsSet = gameStats.keySet();
		
		logger.info("Looping through all games to compare the number of kills on "
				+ "kills_by_means with total_kills");
		for(String gameName : statsSet) {
			Game game = gameStats.get(gameName);
			LinkedHashMap<String, Integer> killsByMeans = game.getKills_by_means();
			
			int totalKillsByMeans = 0;
			Set<String> killsByMeansSet = killsByMeans.keySet();
			for (String mean : killsByMeansSet) {
				totalKillsByMeans += killsByMeans.get(mean);
			}
			int totalKills = game.getTotal_kills();
			logger.info(gameName + " - kills_by_means: " + totalKillsByMeans +
					" - total_kills: " + totalKills);
			
			Assert.assertEquals(totalKillsByMeans, totalKills,
					"Kills by Means is not being calculated correctly in " + gameName);
		}
	}
	
	public void checkIfRankingIsOnDescendingOrder() {
		gameStats = getGameStatsFromLogFile();
		
		LinkedHashMap<String, Integer> playerRanking = sortedPlayerScores();
		Set<String> rankingSet = playerRanking.keySet();
		
        Map.Entry<String, Integer> currentEntry = playerRanking.entrySet().iterator().next();
        int currentScore = currentEntry.getValue();

        logger.info("Comparing player scores");
		for(String player : rankingSet) {
			int score = playerRanking.get(player);
			
			Assert.assertTrue(score <= currentScore, 
					"Player Ranking is not in descending order");
			logger.info(currentScore + " >= " + score);
			
			currentScore = score;
		}
	}
	
}
