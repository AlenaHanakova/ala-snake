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
public class Food extends Thing {

	private static Texture texture;
	private static TextureRegion textureRegion;

	static {
		if (Gdx.files != null) {
			texture = new Texture(Gdx.files.internal("images/foodMouses.png"));
			textureRegion = new TextureRegion(texture);
		}
	}

	public Food(GameModel gameModel, int x, int y) {
		super(gameModel, x, y);
		this.mapMe();
	}

	public void respawn() {
		int oldX = getX();
		int oldY = getY();
		unmapMe();
		generateXAndY();
		while (getGameModel().whosThere(getX(), getY()) != null || (getX() == oldX && getY() == oldY)) {
			System.out.println("Generating new x and y");
			generateXAndY();
		}
		mapMe();
	}

	private void generateXAndY() {
		setX((int) (Math.random() * (double) GameModel.LOGICAL_WIDTH));
		setY((int) (Math.random() * (double) GameModel.LOGICAL_HEIGHT));
		System.out.println("Generated x > " + getX() + ", a y > " + getY());
	}

	public void reset() {
		respawn();
	}

	public void doAction() {
		if (getNextAction() == ActionFlag.RESET) {
			respawn();
			setNextAction(null);
		}
	}

	@Override
	public NetworkThing getNetworkThing() {
		return new NetworkThing(ThingEnum.FOOD, getX(), getY());
	}

	public static void drawStatic(SpriteBatch batch, int x, int y) {
		batch.setColor(Color.WHITE);
		batch.draw(textureRegion, GameModel.getViewCoordinate(x), GameModel.getViewCoordinate(y), GameModel.SIZE_OF_FIELD_TILE / 2, GameModel.SIZE_OF_FIELD_TILE / 2,
				GameModel.SIZE_OF_FIELD_TILE, GameModel.SIZE_OF_FIELD_TILE, 1, 1, 0);
	}

	public void draw(SpriteBatch batch) {
		drawStatic(batch, getX(), getY());
	}
}
