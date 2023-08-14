package com.quakelogparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This class is for creating an object of type Game. It stores all the 
 * 	necessary information for a given game.
 */
public class Game {

	private int total_kills;
	private ArrayList<String> players;
	private LinkedHashMap<String, Integer> kills;

	public Game() {
	}

	public Game(int total_kills, ArrayList<String> players, 
			LinkedHashMap<String, Integer> kills) {
		super();
		this.total_kills = total_kills;
		this.players = players;
		this.kills = kills;
	}

	public int getTotal_kills() {
		return total_kills;
	}

	public void setTotal_kills(int total_kills) {
		this.total_kills = total_kills;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public LinkedHashMap<String, Integer> getKills() {
		return kills;
	}

	public void setKills(LinkedHashMap<String, Integer> kills) {
		this.kills = kills;
	}
}
