package com.alasnake.game;

import com.alasnake.net.NetworkThing;
import com.alasnake.net.ThingEnum;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Alena Hanakova
 */
public class SnakeCell extends Thing {

	private Direction direction;
	private SnakeCellType cellType;
	private Snake snake;

	public SnakeCell(GameModel gameModel, Snake snake, int x, int y, Direction direction, SnakeCellType cellType) {
		super(gameModel, x, y);
		this.snake = snake;
		this.cellType = cellType;
		this.direction = direction;
		if (direction == null) {
			this.direction = Direction.RIGHT;
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	private static TextureRegion loadTextureRegion(SnakeCellType cellType) {
		return cellType.getTextureRegion();
	}

	private static int getRotationOfTexture(Direction direction) {
		int rotation = 0;
		switch (direction) {
			case UP:
				rotation = 90;
				break;
			case LEFT:
				rotation = 180;
				break;
			case DOWN:
				rotation = 270;
				break;
			default:
				break;
		}
		return rotation;
	}

	public SnakeCellType getCellType() {
		return cellType;
	}

	public void setCellType(SnakeCellType cellType) {
		this.cellType = cellType;
	}

	public boolean isHead() {
		return (cellType == SnakeCellType.HEAD);
	}

	public boolean isBody() {
		return (cellType == SnakeCellType.BODY);
	}

	public boolean isTail() {
		return (cellType == SnakeCellType.TAIL);
	}

	public void reset() {
		getGameModel().resetSnake(snake);
	}

	public Snake getSnake() {
		return snake;
	}

	@Override
	public NetworkThing getNetworkThing() {
		ThingEnum thingEnum;
		switch (cellType) {
			case HEAD:
				thingEnum = ThingEnum.SNAKE_HEAD;
				break;
			case BODY:
				thingEnum = ThingEnum.SNAKE_BODY;
				break;
			case CORNER_LEFT:
				thingEnum = ThingEnum.SNAKE_CORNER_LEFT;
				break;
			case CORNER_RIGHT:
				thingEnum = ThingEnum.SNAKE_CORNER_RIGHT;
				break;
			case TAIL:
				thingEnum = ThingEnum.SNAKE_TAIL;
				break;
			default:
				thingEnum = null;
		}
		NetworkThing networkThing = new NetworkThing(thingEnum, getX(), getY());
		networkThing.setDirection(getDirection());
		networkThing.setColor(snake.getColor().toString());
		return networkThing;
	}

	public static void drawStatic(SpriteBatch batch, int x, int y, SnakeCellType cellType, Direction direction) {
		batch.draw(loadTextureRegion(cellType), GameModel.getViewCoordinate(x), GameModel.getViewCoordinate(y), GameModel.SIZE_OF_FIELD_TILE / 2, GameModel.SIZE_OF_FIELD_TILE / 2,
				GameModel.SIZE_OF_FIELD_TILE, GameModel.SIZE_OF_FIELD_TILE, 1, 1, getRotationOfTexture(direction));
	}

	public void draw(SpriteBatch batch) {
		drawStatic(batch, getX(), getY(), cellType, direction);
	}
}



