package com.quakelogparser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogParser {
	
	static String filePath;
	
	public static void logParser() {
		filePath = "src\\main\\resources\\qgames.log";

		String relevantLogs = logFilter(filePath);
		ArrayList<String> listOfGames = separateByGame(relevantLogs);
		
		System.out.println(listOfGames.size() + " games found.\n");
		
	}
	
	public static String logFilter(String filePath) {
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

	public static ArrayList<String> separateByGame(String relevantLogs) {
		String[] gamesArray = relevantLogs.split("\\|");
		List<String> list = Arrays.asList(gamesArray);
		ArrayList<String> listOfGames = new ArrayList<String>(list);
		listOfGames.remove(0);

		return listOfGames;
	}

}
