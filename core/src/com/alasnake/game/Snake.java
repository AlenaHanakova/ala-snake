package com.alasnake.game;

import com.alasnake.net.NetworkThing;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alena Hanakova
 */
public class Snake {

	private static final int INITIAL_LENGTH = 8;
	public static final int WINNER_LENGTH = 30;
	public static final int MINIMUM_LENGTH = 4;

	private Direction direction;
	private final List<SnakeCell> snakeCells;
	private GameModel gameModel;
	private boolean willGrow;
	private Color color;
	private boolean isAlive = true;
	private String userName;

	public Snake(GameModel gameModel, int x, int y, Color color, String userName) {
		this.gameModel = gameModel;
		this.color = color;
		this.userName = userName;
		snakeCells = new LinkedList<SnakeCell>();
		direction = Direction.UP;
		snakeCells.add(new SnakeCell(gameModel, this, x, y, direction, SnakeCellType.HEAD));
		for (int i = 1; i < INITIAL_LENGTH - 1; i++) {
			snakeCells.add(new SnakeCell(gameModel, this, x, y, direction, SnakeCellType.BODY));
		}
		snakeCells.add(new SnakeCell(gameModel, this, x, y, direction, SnakeCellType.TAIL));
	}

	public void draw(SpriteBatch batch) {
		batch.setColor(color);
		synchronized (snakeCells) {
			for (SnakeCell snakeCell : snakeCells) {
				snakeCell.draw(batch);
			}
		}
	}

	/**
	 * Snake changes its direction to the given one, if the given one is not the opposite direction from the current one.
	 *
	 * @param direction New direction of the snake. Cannot be opposite from the current direction.
	 * @return True, if snake changed direction to the given one.
	 */
	public boolean changeDirection(Direction direction) {
		if (direction != this.direction && Direction.getOppositeDirection(direction) != this.direction) {
			this.direction = direction;
			return true;
		}
		return false;
	}

	private SnakeCell getHead() {
		synchronized (snakeCells) {
			return snakeCells.get(0);
		}
	}

	private SnakeCell getTail() {
		synchronized (snakeCells) {
			return snakeCells.get(getTailIndex());
		}
	}

	private int getTailIndex() {
		synchronized (snakeCells) {
			return snakeCells.size() - 1;
		}
	}

	private SnakeCell removeTail() {
		synchronized (snakeCells) {
			SnakeCell snakeCell = snakeCells.remove(getTailIndex());
			snakeCell.unmapMe();
			return snakeCell;
		}
	}

	public void move() {
		if (isAlive) {
			Coordinates front = Movement.getFront(getX(), getY(), direction);
			SnakeCell newHead;
			if (willGrow) {
				newHead = new SnakeCell(gameModel, this, front.getX(), front.getY(), direction, SnakeCellType.HEAD);
				willGrow = false;
			} else {
				newHead = removeTail();
				newHead.setX(front.getX());
				newHead.setY(front.getY());
				newHead.setDirection(direction);
				newHead.setCellType(SnakeCellType.HEAD);
			}
			if (direction == getHead().getDirection()) {
				getHead().setCellType(SnakeCellType.BODY);
			} else {
				setCornerCellDirectionAndType(getHead(), direction);
			}
			getTail().setCellType(SnakeCellType.TAIL);
			newHead.mapMe();
			synchronized (snakeCells) {
				snakeCells.add(0, newHead);
			}
			checkWinner();
			if (!gameModel.isWithinBoundsOfField(front.getX(), front.getY())) {
				gameModel.resetSnake(this);
			}
		}
	}

	private void checkWinner() {
		if (getSize() >= WINNER_LENGTH) {
			gameModel.finish();
		}
	}

	/**
	 * Move snake to the given coordinates and set its new one.
	 */
	public void setSnake(int x, int y, Direction direction) {
		unmapMe();
		for (SnakeCell snakeCell : snakeCells) {
			snakeCell.setX(x);
			snakeCell.setY(y);
		}
		this.direction = direction;
	}

	public void mapMe() {
		synchronized (snakeCells) {
			for (SnakeCell snakeCell : snakeCells) {
				snakeCell.mapMe();
			}
		}
	}

	public void unmapMe() {
		for (SnakeCell snakeCell : snakeCells) {
			snakeCell.unmapMe();
		}
	}

	private void setCornerCellDirectionAndType(SnakeCell snakeCell, Direction headDirection) {
		switch (snakeCell.getDirection()) {
			case RIGHT:
				if (headDirection == Direction.UP) {
					snakeCell.setCellType(SnakeCellType.CORNER_LEFT);
				} else {
					snakeCell.setCellType(SnakeCellType.CORNER_RIGHT);
				}
				break;
			case DOWN:
				if (headDirection == Direction.LEFT) {
					snakeCell.setCellType(SnakeCellType.CORNER_RIGHT);
				} else {
					snakeCell.setCellType(SnakeCellType.CORNER_LEFT);
				}
				break;
			case LEFT:
				if (headDirection == Direction.UP) {
					snakeCell.setCellType(SnakeCellType.CORNER_RIGHT);
				} else {
					snakeCell.setCellType(SnakeCellType.CORNER_LEFT);
				}
				break;
			case UP:
				if (headDirection == Direction.LEFT) {
					snakeCell.setCellType(SnakeCellType.CORNER_LEFT);
				} else {
					snakeCell.setCellType(SnakeCellType.CORNER_RIGHT);
				}
				break;
			default:
		}
		snakeCell.setDirection(headDirection);
	}

	public List<NetworkThing> getNetworkThings() {
		List<NetworkThing> networkThings = new ArrayList<NetworkThing>();
		synchronized (snakeCells) {
			for (SnakeCell snakeCell : snakeCells) {
				networkThings.add(snakeCell.getNetworkThing());
			}
		}
		return networkThings;
	}

	public int getX() {
		return getHead().getX();
	}

	public int getY() {
		return getHead().getY();
	}

	public int getSize() {
		synchronized (snakeCells) {
			return snakeCells.size();
		}
	}

	public void doAction() {
		boolean reset = false;
		synchronized (snakeCells) {
			for (int i = 0; i < snakeCells.size(); i++) {
				ActionFlag nextAction = snakeCells.get(i).getNextAction();
				if (nextAction == ActionFlag.RESET) {
					SnakeCell snakeCell = snakeCells.get(i);
					if (snakeCell.isHead()) {
						reset = true;
						snakeCells.get(i).setNextAction(null);
					} else {
						cellIsHit(i);
					}
				} else if (nextAction == ActionFlag.GROW) {
					willGrow = true;
					snakeCells.get(i).setNextAction(null);
				}
			}
		}
		if (reset) {
			gameModel.resetSnake(this);
		}
	}

	public void fire() {
		if (isAlive) {
			Coordinates front = Movement.getFront(getX(), getY(), direction);
			Laser laser = new Laser(gameModel, direction, front.getX(), front.getY());
			gameModel.addLaser(laser);
			if (gameModel instanceof GameModelServer) {
				GameModelServer gameModelServer = (GameModelServer) gameModel;
				gameModelServer.checkCollisionOnGivenField(laser.getX(), laser.getY());
			}
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public Color getColor() {
		return color;
	}

	public boolean shortenSnake(int numberOfCells) {
		// if the snake would shorten to be shorter than its minimum allowed size, he die
		if (getSize() - MINIMUM_LENGTH < numberOfCells) {
			death();
			return true;
		} else {
			for (int i = 0; i < numberOfCells; i++) {
				removeCell(getSize() - 1);
			}
			return false;
		}
	}

	public void removeCell(int cellNumber) {
		synchronized (snakeCells) {
			snakeCells.get(cellNumber).unmapMe();
			snakeCells.remove(cellNumber);
		}
	}

	public void death() {
		if (isAlive) {
			System.out.println("Snake " + getUserName() + " died with number of snakecells: " + snakeCells.size());
			isAlive = false;
			gameModel.checkWinner();
		}
	}

	public boolean isAlive() {
		return isAlive;
	}

	private boolean checkDeath() {
		if (getSize() < MINIMUM_LENGTH) {
			gameModel.resetSnake(this);
			return true;
		}
		return false;
	}

	private void cellIsHit(int cellIndex) {
		removeCell(cellIndex);
		checkDeath();
	}

	public String getUserName() {
		return userName;
	}
}
