package com.alasnake.game;

import com.alasnake.net.NetworkThing;
import com.alasnake.net.ThingEnum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Alena Hanakova
 */
public class Laser extends Thing {

	private static Texture texture;
	private static TextureRegion textureRegion;
	private Direction direction;
	public static int LOAD = 1;

	static {
		if (Gdx.files != null) {
			texture = new Texture(Gdx.files.internal("images/laser.png"));
			textureRegion = new TextureRegion(texture);
		}
	}

	public Laser(GameModel gameModel, Direction direction, int x, int y) {
		super(gameModel, x, y);
		this.direction = direction;
		mapMe();
	}

	public void move() {
		unmapMe();
		Coordinates front = Movement.getFront(getX(), getY(), direction);
		setX(front.getX());
		setY(front.getY());
		mapMe();
	}

	@Override
	public void reset() {
		unmapMe();
		getGameModel().removeLaser(this);
	}

	public void doAction() {
		if (getNextAction() == ActionFlag.RESET) {
			reset();
			setNextAction(null);
		}
	}

	@Override
	public NetworkThing getNetworkThing() {
		NetworkThing networkThing = new NetworkThing(ThingEnum.LASER, getX(), getY());
		networkThing.setDirection(direction);
		return networkThing;
	}

	public static void drawStatic(SpriteBatch batch, int x, int y, Direction direction) {
		batch.setColor(Color.WHITE);
		batch.draw(textureRegion, GameModel.getViewCoordinate(x), GameModel.getViewCoordinate(y), GameModel.SIZE_OF_FIELD_TILE / 2, GameModel.SIZE_OF_FIELD_TILE / 2,
				GameModel.SIZE_OF_FIELD_TILE, GameModel.SIZE_OF_FIELD_TILE, 1, 1, getRotation(direction));
	}

	public static int getRotation(Direction direction){
		if (direction == Direction.UP || direction == Direction.DOWN){
			return 90;
		}else return 0;
	}

	public void draw(SpriteBatch batch) {
		drawStatic(batch, getX(), getY(),direction);
	}
}
