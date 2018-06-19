package com.alasnake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * @author Alena Hanakova
 */
public class HumanControl implements InputProcessor {

	private Player player;

	/**
	 * Needed for multitouch.
	 */
	private int touchDown = 0;

	public HumanControl(Player player) {
		this.player = player;
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.LEFT) {
			player.changeDirection(Direction.LEFT);
		}
		if (keycode == Input.Keys.RIGHT) {
			player.changeDirection(Direction.RIGHT);
		}
		if (keycode == Input.Keys.DOWN) {
			player.changeDirection(Direction.DOWN);
		}
		if (keycode == Input.Keys.UP) {
			player.changeDirection(Direction.UP);
		}
		if (keycode == Input.Keys.SPACE) {
			player.getSnake().fire();
		}
		if (keycode == Input.Keys.P) {
			player.getGameModel().pauseGame();
		}
		return true;
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

	/**
	 * Control the given snake according to the given touch inputs.
	 *
	 * @param screenX   Logical x where user hit the screen.
	 * @param screenY   Logical y where user hit the screen.
	 * @param touchDown Number of fingers on the screen.
	 * @param snake     The snake to control to.
	 */
	public static void touchScreenControl(int screenX, int screenY, int touchDown, Snake snake) {
		if (touchDown == 1) {
			boolean directionHasChanged = false;
			if (screenX < snake.getX()) {
				directionHasChanged = snake.changeDirection(Direction.LEFT);
			} else if (screenX > snake.getX()) {
				directionHasChanged = snake.changeDirection(Direction.RIGHT);
			}
			if (!directionHasChanged) {
				if (screenY > snake.getY()) {
					snake.changeDirection(Direction.UP);
				} else if (screenY < snake.getY()) {
					snake.changeDirection(Direction.DOWN);
				}
			}
		} else if (touchDown == 2) {
			snake.fire();
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// screenY count from up and we need to count it from down
		screenY = GameModel.VIEW_HEIGHT - screenY;
		touchScreenControl(GameModel.getLogicalCoordinate(screenX), GameModel.getLogicalCoordinate(screenY), touchDown, player.getSnake());
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
