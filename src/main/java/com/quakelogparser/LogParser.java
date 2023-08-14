package com.quakelogparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogParser {
	
	static String filePath;
	
	public static void parseLog(int index) {
		filePath = "src\\main\\resources\\qgames.log";

		String relevantLogs = filterLog(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);
		
		System.out.println(listOfGames.size() + " games found.\n");
		
		ArrayList<String> listOfPlayers = identifyPlayersOnGame(listOfGames.get(index));
		ArrayList<String> listOfKills = identifyKillsOnGame(listOfGames.get(index));
		
		System.out.println("Players on game " + index + ": " + listOfPlayers);
		System.out.println("Kills on game " + index + ": " + listOfKills);
		
	}
	
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

	private static ArrayList<String> separateByGame(String relevantLogs) {
		String[] gamesArray = relevantLogs.split("\\|");
		List<String> list = Arrays.asList(gamesArray);
		ArrayList<String> listOfGames = new ArrayList<String>(list);
		listOfGames.remove(0);
		return listOfGames;
	}
	
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
