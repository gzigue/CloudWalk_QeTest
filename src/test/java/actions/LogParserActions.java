package actions;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.testng.Assert;

import com.quakelogparser.Game;
import com.quakelogparser.LogParser;

public class LogParserActions extends LogParser{
	
	private String filePath = "src\\main\\resources\\qgames.log";
	private LinkedHashMap<String, Game> gameStats;
	
	public LogParserActions() {}
	
	public LinkedHashMap<String, Game> getGameStatsFromLogFile() {
		String relevantLogs = filterLog(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);

		return calculateGameStats(listOfGames);
	}
	
	public int getTotalKillsFromGame1() {
		gameStats = getGameStatsFromLogFile();
		return gameStats.get("game_1").getTotal_kills();
	}
	
	public void verifyThereAreNoKillsOnGame1() {
		Assert.assertEquals(getTotalKillsFromGame1(), 0);
	}

}
