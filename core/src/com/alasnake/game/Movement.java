package com.alasnake.game;

/**
 * @author Alena Hanakova
 */
public class Movement {

	public static Coordinates getFront(int x, int y, Direction direction) {
		Coordinates coordinates;
		switch (direction) {
			case LEFT:
				coordinates = new Coordinates(x - 1, y);
				break;
			case RIGHT:
				coordinates = new Coordinates(x + 1, y);
				break;
			case DOWN:
				coordinates = new Coordinates(x, y - 1);
				break;
			case UP:
				coordinates = new Coordinates(x, y + 1);
				break;
			default:
				coordinates = null;
		}
		return coordinates;
	}
}
