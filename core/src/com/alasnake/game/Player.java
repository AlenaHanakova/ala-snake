package com.alasnake.game;

/**
 * @author Alena Hanakova
 */
public class Player {

	public enum Control {
		KEYBOARD, AI, CLIENT
	}

	private GameModelServer gameModel;
	private Snake snake;
	private Direction newDirection;
	private AIControl aiControl;

	public Player(GameModelServer gameModel, Snake snake, Control control) {
		this.gameModel = gameModel;
		this.snake = snake;
		if (control == Control.KEYBOARD) {
			new HumanControl(this);
		} else if (control == Control.AI) {
			aiControl = new AIControl(this);
		}
	}

	public void changeDirection(Direction newDirection) {
		if (newDirection != null) {
			this.newDirection = newDirection;
		}
	}

	public Snake getSnake() {
		return snake;
	}

	public GameModelServer getGameModel() {
		return gameModel;
	}

	public void move() {
		if (aiControl != null) {
			newDirection = aiControl.computeDirection();
		}
		if (newDirection != null) {
			snake.changeDirection(newDirection);
		}
		snake.move();
		newDirection = null;
	}
}
