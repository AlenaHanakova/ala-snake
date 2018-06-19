package com.alasnake.game;

/**
 * @author Alena Hanakova
 */
public enum Direction {

	LEFT, RIGHT, DOWN, UP;

	public static Direction getOppositeDirection(Direction direction) {
		switch (direction) {
			case UP:
				return Direction.DOWN;
			case DOWN:
				return Direction.UP;
			case LEFT:
				return Direction.RIGHT;
			case RIGHT:
				return Direction.LEFT;
		}
		return null;
	}
}
