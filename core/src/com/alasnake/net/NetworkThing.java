package com.alasnake.net;

import com.alasnake.game.Direction;

/**
 * @author Alena Hanakova
 */
public class NetworkThing {

	private ThingEnum thingEnum;
	private int x;
	private int y;
	private Direction direction;
	private String color;

	public NetworkThing() {
	}

	public NetworkThing(ThingEnum thingEnum, int x, int y) {
		this.thingEnum = thingEnum;
		this.x = x;
		this.y = y;
	}

	public ThingEnum getThingEnum() {
		return thingEnum;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
