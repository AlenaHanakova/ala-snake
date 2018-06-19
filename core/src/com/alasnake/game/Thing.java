package com.alasnake.game;

import com.alasnake.net.NetworkThing;

/**
 * @author Alena Hanakova
 */
public abstract class Thing {
	private GameModel gameModel;
	private int x;
	private int y;
	private ActionFlag nextAction;

	public Thing(GameModel gameModel, int x, int y) {
		this.gameModel = gameModel;
		this.x = x;
		this.y = y;
	}

	public boolean mapMe() {
		if (gameModel.isWithinBoundsOfField(getX(), getY())) {
			gameModel.mapThing(this);
			return true;
		}
		return false;
	}

	public void unmapMe() {
		if (gameModel.isWithinBoundsOfField(getX(), getY())) {
			gameModel.unmapThing(this);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public GameModel getGameModel() {
		return gameModel;
	}

	public abstract NetworkThing getNetworkThing();

	public abstract void reset();

	public ActionFlag getNextAction() {
		return nextAction;
	}

	public void setNextAction(ActionFlag nextAction) {
		this.nextAction = nextAction;
	}
}

