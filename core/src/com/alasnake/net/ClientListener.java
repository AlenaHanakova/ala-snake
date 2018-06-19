package com.alasnake.net;

import com.alasnake.game.PlayerInfo;
import com.alasnake.game.Statistics;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.util.List;

/**
 * @author Alena Hanakova
 */
public class ClientListener extends Listener {

	SnakeClient client;

	public ClientListener(SnakeClient client) {
		this.client = client;
	}

	@Override
	public void connected(Connection connection) {
		PlayerInfo playerInfo = new PlayerInfo(client.getPlayerName());
		client.sendTCP(playerInfo);
		System.out.println("Connected to server.");
	}

	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof List) {
			client.networkThings = (List<NetworkThing>) object;
		} else if (object instanceof Statistics) {
			client.getGameModelClient().statistics = (Statistics) object;
		}
	}
}
