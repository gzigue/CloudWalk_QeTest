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
		
		String[] gamesArray = relevantLogs.toString().split("\\|");
		List<String> list = Arrays.asList(gamesArray);
		
		ArrayList<String> listOfGames = new ArrayList<String>(list);
		listOfGames.remove(0);
		
		System.out.println(listOfGames.size() + " games found.\n");
		
	}
}
