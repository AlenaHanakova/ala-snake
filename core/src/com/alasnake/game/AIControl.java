package com.alasnake.game;

/**
 * @author Alena Hanakova
 */
public class AIControl {

	private Snake snake;
	private GameModel gameModel;

	public AIControl(Player player) {
		snake = player.getSnake();
		gameModel = player.getGameModel();
	}

	public Direction computeDirection() {
		Direction computedDirection = snake.getDirection();
		int foodX = gameModel.getFood().getX();
		int foodY = gameModel.getFood().getY();
		switch (snake.getDirection()) {
			case LEFT:
				if (foodX < snake.getX() || snake.getY() == 0) {
					//do nothing - continue the same direction
				} else {
					if (foodY > snake.getY()) {
						computedDirection = Direction.UP;
					} else {
						computedDirection = Direction.DOWN;
					}
				}
				break;
			case RIGHT:
				if (foodX > snake.getX()) {
					//do nothing - continue the same direction
				} else {
					if (foodY > snake.getY() || snake.getY() == 0) {
						computedDirection = Direction.UP;
					} else {
						computedDirection = Direction.DOWN;
					}
				}
				break;
			case DOWN:
				if (foodY < snake.getY()) {
					//do nothing - continue the same direction
				} else {
					if (foodX > snake.getX() || snake.getX() == 0) {
						computedDirection = Direction.RIGHT;
					} else {
						computedDirection = Direction.LEFT;
					}
				}
				break;
			case UP:
				if (foodY > snake.getY()) {
					//do nothing - continue the same direction
				} else {
					if (foodX > snake.getX() || snake.getX() == 0) {
						computedDirection = Direction.RIGHT;
					} else {
						computedDirection = Direction.LEFT;
					}
				}
				break;
		}
		return computedDirection;
	}
}
