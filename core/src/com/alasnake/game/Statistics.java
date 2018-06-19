package com.alasnake.game;

import java.util.List;

/**
 * @author Alena Hanakova
 */
public class Statistics {

	private List<String> userNames;
	private int[] snakeSizes;

	public Statistics() {
	}

	public Statistics(List<String> userNames, int[] snakeSizes) {
		this.userNames = userNames;
		this.snakeSizes = snakeSizes;
	}

	public List<String> getUserNames() {
		return userNames;
	}

	public int[] getSnakeSizes() {
		return snakeSizes;
	}
}
