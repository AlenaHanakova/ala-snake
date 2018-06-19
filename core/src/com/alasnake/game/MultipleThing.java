package com.alasnake.game;

import com.alasnake.net.NetworkThing;
import com.alasnake.net.ThingEnum;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alena Hanakova
 */
public class MultipleThing extends Thing {

	private List<Thing> things;

	public MultipleThing(GameModel gameModel, int x, int y) {
		super(gameModel, x, y);
		things = new ArrayList<Thing>();
	}

	public int getNumberOfThings() {
		return things.size();
	}

	public Thing getThingOnPosition(int i) {
		return things.get(i);
	}

	public Thing getTheOnlyThing() {
		if (things.size() == 1) {
			return things.get(0);
		}
		return null;
	}

	public void addThing(Thing thing) {
		things.add(thing);
	}

	public void removeThing(Thing thing) {
		things.remove(thing);
	}

	public void reset() {
	}

	@Override
	public NetworkThing getNetworkThing() {
		return null;
	}
}
