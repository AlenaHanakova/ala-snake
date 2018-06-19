package com.alasnake.game;

import com.alasnake.net.NetworkCommand;
import com.alasnake.net.SnakeClient;
import com.alasnake.screen.FinishScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Alena Hanakova
 */
public class GameModelClient extends GameModel implements InputProcessor {

	private SnakeClient client;
	public Statistics statistics;
	private int touchDown = 0;

	public GameModelClient(SpaceSnake game, String username, SnakeClient client) {
		super(game, username);
		this.client = client;
		Gdx.input.setInputProcessor(this);
		client.setGameModelClient(this);
	}

	@Override
	public void draw(SpriteBatch batch) {
		SpaceSnake.backgroundSprite.draw(batch);
		client.draw(batch);
		if (statistics != null) {
			clientFinish(statistics);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.LEFT) {
			client.sendTCP(NetworkCommand.GO_LEFT);
		}
		if (keycode == Input.Keys.RIGHT) {
			client.sendTCP(NetworkCommand.GO_RIGHT);
		}
		if (keycode == Input.Keys.DOWN) {
			client.sendTCP(NetworkCommand.GO_DOWN);
		}
		if (keycode == Input.Keys.UP) {
			client.sendTCP(NetworkCommand.GO_UP);
		}
		if (keycode == Input.Keys.SPACE) {
			client.sendTCP(NetworkCommand.FIRE);
		}
		return true;
	}

	public void clientFinish(Statistics statistics) {
		game.setScreen(new FinishScreen(game, statistics));
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchDown++;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// screenY count from up and we need to count it from down
		screenY = GameModel.VIEW_HEIGHT - screenY;
		client.sendTCP(new Touch(GameModel.getLogicalCoordinate(screenX), GameModel.getLogicalCoordinate(screenY), touchDown));
		touchDown = 0;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
