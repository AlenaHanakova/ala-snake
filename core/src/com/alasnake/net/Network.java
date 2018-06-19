package com.alasnake.net;

import com.alasnake.game.Coordinates;
import com.alasnake.game.Direction;
import com.alasnake.game.PlayerInfo;
import com.alasnake.game.Statistics;
import com.alasnake.game.Touch;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alena Hanakova
 */
public class Network {

	public static final int TCP_PORT = 42420;
	public static final int UDP_PORT = 42421;

	public static void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(NetworkThing.class);
		kryo.register(List.class);
		kryo.register(ArrayList.class);
		kryo.register(Direction.class);
		kryo.register(ThingEnum.class);
		kryo.register(NetworkCommand.class);
		kryo.register(Coordinates.class);
		kryo.register(PlayerInfo.class);
		kryo.register(String.class);
		kryo.register(Statistics.class);
		kryo.register(int[].class);
		kryo.register(Touch.class);
	}
}
