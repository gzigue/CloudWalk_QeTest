package com.quakelogparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class contains all the logic for the Log Parser. It reads the file,
 * 	process the information and returns it in a format the user can understand.
 */
public class LogParser {
		
	static LinkedHashMap<String, Game> gameStats;
	
	/**
     * Parses the log. This method calls the other methods and prints
     * 	the result.
     */
	public static void parseLog(String filePath) {

		String relevantLogs = filterLog(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);
		
		System.out.println(listOfGames.size() + " games found.\n");
		
		gameStats = calculateGameStats(listOfGames);
	}
	
	/**
     * Filters the log, removing the unnecessary lines and adding the rest
     * 	to a single String.
     *
     * @param String filePath - The relative path to the qgames.log file.
     * @return A single String with the relevant information from the log.
     */
	private static String filterLog(String filePath) {
		StringBuilder relevantLogs = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.contains("InitGame")) {
					relevantLogs.append('|');
				} else if (line.contains("Kill:") || line.contains("ClientUserinfoChanged:")) {
					relevantLogs.append(line);
					relevantLogs.append(",");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return relevantLogs.toString();
	}

	/**
     * Gets the relevant information from the log and separates it game by
     * 	game, on a list.
     *
     * @param String relevantLogs - A single String containing the relevant
     * 	information from the log.
     * @return A list of Strings, each one containing information from a 
     * 	different game.
     */
	private static ArrayList<String> separateByGame(String relevantLogs) {
		String[] gamesArray = relevantLogs.split("\\|");
		List<String> list = Arrays.asList(gamesArray);
		ArrayList<String> listOfGames = new ArrayList<String>(list);
		listOfGames.remove(0);
		return listOfGames;
	}
	
	/**
     * Uses the game information to identify the kills.
     *
     * @param String game - A String containing information from a game.
     * @return A list of Strings, each one containing a line from the log
     * 	where a kill was identified.
     */
	public static ArrayList<String> identifyKillsOnGame(String game) {
		ArrayList<String> listOfKills = new ArrayList<String>();
		String[] lines = game.split(",");
		
		for (String line : lines) {
			if (line.contains("Kill:")) {
				listOfKills.add(line);
			}
		}
		return listOfKills;
	}
	
	/**
     * Uses the game information to get a list of players.
     *
     * @param String game - A String containing information from a game.
     * @return A list of Strings, each one containing a player's user name.
     */
	public static ArrayList<String> identifyPlayersOnGame(String game) {
		ArrayList<String> listOfPlayers = new ArrayList<String>();
		String[] lines = game.split(",");
		
		for (String line : lines) {
			if (line.contains("ClientUserinfoChanged:")) {
				String player = line.split("\\\\")[1];
				if (!listOfPlayers.contains(player)) {
					listOfPlayers.add(player);
				}
			}
		}
		return listOfPlayers;
	}
	
	/**
     * Uses the kill information to get the killer player.
     *
     * @param String kill - A String containing a line from the log.
     * @return A String, either containing "<world>" or an user name.
     */
	public static String identifyScoringPlayer(String kill) {
		String relevantInfo = kill.split(":")[3].trim();
		String scoringPlayer = relevantInfo.split("killed")[0].trim();
		
		return scoringPlayer;
	}
	
	/**
     * Uses the kill information to get the player who died.
     *
     * @param String kill - A String containing a line from the log.
     * @return A String containing an user name.
     */
	public static String identifyVictim(String kill) {
		String relevantInfo = kill.split(":")[3];
		String victim = relevantInfo.split("killed")[1].split("by")[0].trim();
		
		return victim;
	}

	/**
     * Uses the list of games to calculate the status for every game.
     * 	Identifies who killed, who died and how many points each player scored.
     *
     * @param ArrayList<String> listOfGames - A list of Strings containing 
     * 	information from all the games.
     * @return A Linked HashMap (or dictionary). It's linked so it returns the
     * 	games in the order they were stored. The keys contain the game number,
     * 	and the values are objects of type Game.
     */
	public static LinkedHashMap<String, Game> calculateGameStats(ArrayList<String> listOfGames) {
		ArrayList<String> listOfKills;
		Game game;
		LinkedHashMap<String, Game> gameStats = new LinkedHashMap<String, Game>();

		for (String gameString : listOfGames) {
			listOfKills = identifyKillsOnGame(gameString);
			
			int index = listOfGames.indexOf(gameString)+1;
			int total_kills = listOfKills.size();
			
			ArrayList<String> players = identifyPlayersOnGame(gameString);
			LinkedHashMap<String, Integer> kills = new LinkedHashMap<String, Integer>();
			
			for (String player : players) {
				kills.put(player, 0);
			}
			
			for (String kill : listOfKills) {
				String scoringPlayer = identifyScoringPlayer(kill);
				String victim = identifyVictim(kill);
				
				if (scoringPlayer.equals("<world>")) {
					int victimScore = kills.get(victim);
					kills.put(victim, victimScore-1);
				}
				else if (players.size() > 0) {
					int score = kills.get(scoringPlayer);
					kills.put(scoringPlayer, score+1);
				}
			}
			
			game = new Game(total_kills, players, kills);
			gameStats.put("game_"+index ,game);
		}
		return gameStats;
	}

}
