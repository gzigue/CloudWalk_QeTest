package com.quakelogparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains all the logic for the Log Parser. It reads the file,
 * 	process the information and returns it in a format the user can understand.
 */
public class LogParser {
		
	/**
     * Parses the log. This method calls the other methods and prints
     * 	the result.
     */
	public static void parseLog(String filePath) {

		String relevantLogs = filterLog(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);
		
		System.out.println(listOfGames.size() + " games found.\n");		
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

}
