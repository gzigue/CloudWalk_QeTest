package com.quakelogparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * This class contains all the logic for the Log Parser. It reads the file,
 * process the information and returns it in a format the user can understand.
 */
public class LogParser {

	static LinkedHashMap<String, Game> gameStats;
	static LinkedHashMap<String, Integer> playerScores = new LinkedHashMap<String, Integer>();

	/**
	 * Parses the log. This method calls the other methods and prints the result.
	 */
	public static void parseLog(String filePath) {

		// Filtering the logs and separating them by game
		String relevantLogs = filterLog(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);

		// Calculating the game statistics and parsing them in Json format
		gameStats = calculateGameStats(listOfGames);
		String formattedStats = formatGameStats(gameStats);

		// Creating a player ranking by sorting the players' total scores
		LinkedHashMap<String, Integer> playerRanking = sortedPlayerScores();

		// Printing the information to the screen
		System.out.println(listOfGames.size() + " games found.\n");
		System.out.println(formattedStats + "\n");
		System.out.println("Player Ranking:");
		System.out.println(formatPlayerScores(playerRanking));
	}

	/**
	 * Filters the log, removing the unnecessary lines and adding the rest to a
	 * single String.
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
	 * Gets the relevant information from the log and separates it game by game, on
	 * a list.
	 *
	 * @param String relevantLogs - A single String containing the relevant
	 *        information from the log.
	 * @return A list of Strings, each one containing information from a different
	 *         game.
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
	 * @return A list of Strings, each one containing a line from the log where a
	 *         kill was identified.
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
	 * Uses the kill information to get the means of killing.
	 *
	 * @param String kill - A String containing a line from the log.
	 * @return A String containing the means of killing.
	 */
	public static String identifyMeansOfKilling(String kill) {
		String relevantInfo = kill.split(":")[3];
		String meansOfKilling = relevantInfo.split("killed")[1].split("by")[1].trim();

		return meansOfKilling;
	}

	/**
	 * Uses the list of games to calculate the status for every game. Identifies who
	 * killed, who died and how many points each player scored.
	 *
	 * @param ArrayList<String> listOfGames - A list of Strings containing
	 *        information from all the games.
	 * @return A Linked HashMap (or dictionary). It's linked so it returns the games
	 *         in the order they were stored. The keys contain the game number, and
	 *         the values are objects of type Game.
	 */
	public static LinkedHashMap<String, Game> calculateGameStats(ArrayList<String> listOfGames) {
		ArrayList<String> listOfKills;
		Game game;
		LinkedHashMap<String, Game> gameStats = new LinkedHashMap<String, Game>();

		for (String gameString : listOfGames) {
			listOfKills = identifyKillsOnGame(gameString);

			int index = listOfGames.indexOf(gameString) + 1;
			int total_kills = listOfKills.size();

			ArrayList<String> players = identifyPlayersOnGame(gameString);
			LinkedHashMap<String, Integer> kills = new LinkedHashMap<String, Integer>();
			LinkedHashMap<String, Integer> kills_by_means = new LinkedHashMap<String, Integer>();

			for (String player : players) {
				kills.put(player, 0);
				if (!playerScores.containsKey(player)) {
					playerScores.put(player, 0);
				}
			}

			for (String kill : listOfKills) {
				String scoringPlayer = identifyScoringPlayer(kill);
				String victim = identifyVictim(kill);
				String meansOfKilling = identifyMeansOfKilling(kill);

				if (scoringPlayer.equals("<world>")) {
					int victimScore = kills.get(victim);
					kills.put(victim, victimScore - 1);

					int victimTotalScore = playerScores.get(victim);
					playerScores.put(victim, victimTotalScore - 1);
				} else if (players.size() > 0) {
					int score = kills.get(scoringPlayer);
					kills.put(scoringPlayer, score + 1);

					int totalScore = playerScores.get(scoringPlayer);
					playerScores.put(scoringPlayer, totalScore + 1);
				}
				
				if (!kills_by_means.containsKey(meansOfKilling)) {
					kills_by_means.put(meansOfKilling, 1);
				}
				else {
					int meansCounter = kills_by_means.get(meansOfKilling);
					kills_by_means.put(meansOfKilling, meansCounter+1);
				}
			}

			game = new Game(total_kills, players, kills, kills_by_means);
			gameStats.put("game_" + index, game);
		}
		return gameStats;
	}

	/**
	 * Gets the game stats and formats it, so they can be printed in a readable Json
	 * format.
	 *
	 * @param LinkedHashMap<String, Game> gameStats - A dictionary containing the
	 *        game numbers and the Game objects.
	 * @return A single String containing all the relevant log information in Json
	 *         format.
	 */
	public static String formatGameStats(LinkedHashMap<String, Game> gameStats) {
		Gson gson = new Gson();
		String stat = gson.toJson(gameStats);

		Gson newGson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = JsonParser.parseString(stat);
		String prettyJson = newGson.toJson(jsonElement);

		return prettyJson;
	}

	/**
	 * Gets a LinkedHashMap and parses it to a readable Json format.
	 *
	 * @param LinkedHashMap<String, Game> playerScores - A dictionary containing
	 * 	      all the players in the log and their total scores.
	 * @return A single String containing player user names and total scores in 
	 * 		   Json format.
	 */
	public static String formatPlayerScores(LinkedHashMap<String, Integer> playerScores) {
		Gson gson = new Gson();
		String scores = gson.toJson(playerScores);

		Gson newGson = new GsonBuilder().setPrettyPrinting().create();
		JsonElement jsonElement = JsonParser.parseString(scores);
		String prettyJson = newGson.toJson(jsonElement);

		return prettyJson;
	}

	/**
	 * Gets the playerScores LinkedHashMap and sorts it by value, in descending order.
	 *
	 * @return A LinkedHashMap containing player user names and total scores in 
	 * 		   descending order.
	 */
	public static LinkedHashMap<String, Integer> sortedPlayerScores() {
		List<Map.Entry<String, Integer>> entries = new ArrayList<>(playerScores.entrySet());

		Collections.sort(entries, (entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

		LinkedHashMap<String, Integer> sortedScores = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : entries) {
			sortedScores.put(entry.getKey(), entry.getValue());
		}
		return sortedScores;
	}

}
