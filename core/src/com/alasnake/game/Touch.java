package com.alasnake.game;

/**
 * @author Alena Hanakova
 */
public class Touch {

	private int x;
	private int y;
	private int touchDown;

	public Touch() {
	}

	public Touch(int x, int y, int touchDown) {
		this.x = x;
		this.y = y;
		this.touchDown = touchDown;
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

	public int getTouchDown() {
		return touchDown;
	}

	public void setTouchDown(int touchDown) {
		this.touchDown = touchDown;
	}
}
