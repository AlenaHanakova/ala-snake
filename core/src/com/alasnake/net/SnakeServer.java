package com.alasnake.net;

import com.alasnake.game.Direction;
import com.alasnake.game.GameModel;
import com.alasnake.game.HumanControl;
import com.alasnake.game.Player;
import com.alasnake.game.PlayerInfo;
import com.alasnake.game.Snake;
import com.alasnake.game.Touch;
import com.alasnake.screen.LobbyScreen;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alena Hanakova
 */
public class SnakeServer extends Server {

	public enum STATE {
		LOBBY, GAME
	}

	/**
	 * Player info map contains all players connected in lobby.
	 * This map creates this server when new client is connected.
	 */
	private Map<Integer, PlayerInfo> playerInfoList = new HashMap<Integer, PlayerInfo>();

	/**
	 * Map of clients contains all players in the game and their connection id's as the key.
	 * This map is set from the game model.
	 */
	private Map<Integer, Player> clientsMap;

	private STATE state;

	public SnakeServer() {
		state = STATE.LOBBY;
		clientsMap = new HashMap<Integer, Player>(GameModel.NUMBER_OF_PLAYERS);
	}

	public void setState(STATE state) {
		this.state = state;
	}

	public Map<Integer, PlayerInfo> getPlayerInfoList() {
		return playerInfoList;
	}

	public void putClient(int connectionId, Player player) {
		clientsMap.put(connectionId, player);
	}

	@Override
	public void start() {
		Network.register(this);
		addListener(new Listener() {

			@Override
			public void received(Connection c, Object object) {
				switch (state) {
					case LOBBY:
						processLobbyCommandFromClient(object, c.getID());
						break;
					case GAME:
						Player player =  clientsMap.get(c.getID());
						if (player != null) {
							processGameCommandFromClient(object, player.getSnake());
							break;
						} else {
							System.out.println("Ignoring data from not registered client.");
						}
				}
			}
		});
		try {
			System.out.println("Binding to TCP port " + Network.TCP_PORT + " and UDP port " + Network.UDP_PORT);
			bind(Network.TCP_PORT, Network.UDP_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.start();
	}

	private void processGameCommandFromClient(Object object, Snake snake) {
		if (object instanceof NetworkCommand) {
			NetworkCommand networkCommand = (NetworkCommand) object;
			switch (networkCommand) {
				case GO_LEFT:
					snake.changeDirection(Direction.LEFT);
					break;
				case GO_RIGHT:
					snake.changeDirection(Direction.RIGHT);
					break;
				case GO_DOWN:
					snake.changeDirection(Direction.DOWN);
					break;
				case GO_UP:
					snake.changeDirection(Direction.UP);
					break;
				case FIRE:
					snake.fire();
					break;
			}
		} else if (object instanceof Touch) {
			Touch touch = (Touch) object;
			HumanControl.touchScreenControl(touch.getX(), touch.getY(), touch.getTouchDown(), snake);
		}
	}

	private void processLobbyCommandFromClient(Object object, int id) {
		if (object instanceof PlayerInfo) {
			PlayerInfo playerInfo = (PlayerInfo) object;
			playerInfoList.put(id, playerInfo);
			System.out.println("New client with id " + id + " and name " + playerInfo.getName() + " has been connected");
			LobbyScreen.clients += "Player " + playerInfo.getName() +  " has been connected." + "\n";
		}
	}
}
