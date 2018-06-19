package com.alasnake.game;

import com.alasnake.net.SnakeClient;
import com.alasnake.screen.FinishScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alena Hanakova
 */
public class GameModel implements Screen {

	public static final int SIZE_OF_FIELD_TILE;

	public static final int LOGICAL_WIDTH = 48;
	public static final int LOGICAL_HEIGHT = 27;

	public static final int VIEW_WIDTH;
	public static final int VIEW_HEIGHT;

	public static final int NUMBER_OF_PLAYERS = 4;

	protected final SpaceSnake game;
	private SpriteBatch batch;
	public List<Snake> snakes;
	protected final List<Laser> lasers;
	protected Food food;
	protected Thing[][] fieldOfThings;
	protected List<MultipleThing> multipleThings;

	protected SnakeClient client;
	protected List<Integer> startPositions;

	static {
		if (Gdx.graphics != null) {
			VIEW_WIDTH = Gdx.graphics.getWidth();
			VIEW_HEIGHT = Gdx.graphics.getHeight();
		} else {
			VIEW_WIDTH = 50;
			VIEW_HEIGHT = 50;
		}
		if (VIEW_WIDTH / LOGICAL_WIDTH > VIEW_HEIGHT / LOGICAL_HEIGHT) {
			SIZE_OF_FIELD_TILE = VIEW_HEIGHT / LOGICAL_HEIGHT;
		} else {
			SIZE_OF_FIELD_TILE = VIEW_WIDTH / LOGICAL_WIDTH;
		}
	}

	public GameModel(SpaceSnake game, String username) {
		this.game = game;

		// need to call this to load the class from this graphics thread, otherwise we could get NoClassDefFoundError or ExceptionInInitializerError in multiplayer when we get fire command from client.
		int load = Laser.LOAD;

		fieldOfThings = new Thing[LOGICAL_WIDTH][LOGICAL_HEIGHT];
		multipleThings = new ArrayList<MultipleThing>();

		snakes = new ArrayList<Snake>();
		lasers = new ArrayList<Laser>();
		createStartPositions();
	}

	public void initGraphics() {
		batch = new SpriteBatch();
	}

	private void createStartPositions() {
		startPositions = new ArrayList<Integer>(NUMBER_OF_PLAYERS);
		int base = LOGICAL_WIDTH / (NUMBER_OF_PLAYERS + 1);
		for (int i = 1; i < NUMBER_OF_PLAYERS + 1; i++) {
			startPositions.add(base * i);
		}
	}

	public void resetSnake(Snake snake) {
		Collections.shuffle(startPositions);
		snake.shortenSnake(1);
		snake.setSnake(startPositions.get(0), -1, Direction.UP);
	}

	/**
	 * Get view coordinate according to logical coordinate.
	 *
	 * @param logicalCoordinate The logical coordinate to transfer.
	 * @return View coordinate in the game area.
	 */
	public static int getViewCoordinate(int logicalCoordinate) {
		return logicalCoordinate * SIZE_OF_FIELD_TILE;
	}

	public static int getLogicalCoordinate(int viewCoordinate) {
		return viewCoordinate / SIZE_OF_FIELD_TILE;
	}

	public void mapThing(Thing newThing) {
		Thing oldThing = fieldOfThings[newThing.getX()][newThing.getY()];
		if (oldThing == null) {
			fieldOfThings[newThing.getX()][newThing.getY()] = newThing;
		} else {
			if (oldThing instanceof MultipleThing) {
				MultipleThing multipleThing = (MultipleThing) oldThing;
				multipleThing.addThing(newThing);
			} else {
				MultipleThing multipleThing = new MultipleThing(this, newThing.getX(), newThing.getY());
				multipleThing.addThing(oldThing);
				multipleThing.addThing(newThing);
				fieldOfThings[newThing.getX()][newThing.getY()] = multipleThing;
				multipleThings.add(multipleThing);
			}
		}
	}

	public void unmapThing(Thing thing) {
		Thing oldThing = fieldOfThings[thing.getX()][thing.getY()];
		if (oldThing instanceof MultipleThing) {
			MultipleThing multipleThing = (MultipleThing) oldThing;
			multipleThing.removeThing(thing);
			Thing remainingThing = multipleThing.getTheOnlyThing();
			if (remainingThing != null) {
				fieldOfThings[thing.getX()][thing.getY()] = remainingThing;
				multipleThings.remove(multipleThing);
			}
		} else {
			fieldOfThings[thing.getX()][thing.getY()] = null;
		}
	}

	public Thing whosThere(int x, int y) {
		return fieldOfThings[x][y];
	}

	public boolean isWithinBoundsOfField(int x, int y) {
		if (x >= 0 && x < LOGICAL_WIDTH && y >= 0 && y < LOGICAL_HEIGHT) {
			return true;
		}
		return false;
	}

	public Food getFood() {
		return food;
	}

	public void addLaser(Laser laser) {
		synchronized (lasers) {
			lasers.add(laser);
		}
	}

	public void removeLaser(Laser laser) {
		synchronized (lasers) {
			lasers.remove(laser);
		}
	}

	/**
	 * Pokud zbyl už jen jediný had naživu, stane se vítězem.
	 *
	 * @return <tt> true </tt> pokud se jediný zbylý had stal vítězem.
	 */
	public void checkWinner() {
		Snake last = null;
		for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
			if (snakes.get(i).isAlive()) {
				if (last == null) {
					last = snakes.get(i);
				} else {
					// if the second live snake is found
					return;
				}
			}
		}
		if (last != null) {
			finish();
		} else {
			System.err.println("All snakes are dead.");
		}
	}

	@Override
	public void dispose() {
		if (batch != null) {
			batch.dispose();
		}
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		draw(batch);
		batch.end();
	}

	public void draw(SpriteBatch batch) {
		SpaceSnake.backgroundSprite.draw(batch);
		synchronized (lasers) {
			for (int i = 0; i < lasers.size(); i++) {
				lasers.get(i).draw(batch);
			}
		}
		for (Snake snake : snakes) {
			snake.draw(batch);
		}
		food.draw(batch);
	}

	public void finish() {
		dispose();
	}



	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}
}

