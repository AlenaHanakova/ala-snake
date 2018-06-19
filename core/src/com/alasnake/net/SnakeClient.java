package com.alasnake.net;

import com.alasnake.game.Food;
import com.alasnake.game.GameModel;
import com.alasnake.game.GameModelClient;
import com.alasnake.game.Laser;
import com.alasnake.game.Snake;
import com.alasnake.game.SnakeCell;
import com.alasnake.game.SnakeCellType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.kryonet.Client;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alena Hanakova
 */
public class SnakeClient extends Client {

	public List<NetworkThing> networkThings = new ArrayList<NetworkThing>();
	private String ipAddress;
	private GameModelClient gameModelClient;
	private String playerName;

	public SnakeClient() {
		super.start();
		Network.register(this);
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public InetAddress discoverHost() {
		return discoverHost(Network.UDP_PORT, 1000);
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void connect() {
		addListener(new ClientListener(this));
		new Thread("Connect to server thread") {

			@Override
			public void run() {
				try {
					connect(5000, ipAddress, Network.TCP_PORT, Network.UDP_PORT);
					System.out.println("Successfully connected to server.");
				} catch (IOException ex) {
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}.start();
	}

	public GameModelClient getGameModelClient() {
		return gameModelClient;
	}

	public void setGameModelClient(GameModelClient gameModelClient) {
		this.gameModelClient = gameModelClient;
	}

	public void draw(SpriteBatch batch) {
		synchronized (networkThings) {
			for (NetworkThing networkThing : networkThings) {
				ThingEnum thingEnum = networkThing.getThingEnum();
				if (networkThing.getColor() != null) {
					batch.setColor(Color.valueOf(networkThing.getColor()));
				}
				switch (thingEnum) {
					case FOOD:
						Food.drawStatic(batch, networkThing.getX(), networkThing.getY());
						break;
					case LASER:
						Laser.drawStatic(batch, networkThing.getX(), networkThing.getY(), networkThing.getDirection());
						break;
					case SNAKE_HEAD:
						SnakeCell.drawStatic(batch, networkThing.getX(), networkThing.getY(), SnakeCellType.HEAD, networkThing.getDirection());
						break;
					case SNAKE_BODY:
						SnakeCell.drawStatic(batch, networkThing.getX(), networkThing.getY(), SnakeCellType.BODY, networkThing.getDirection());
						break;
					case SNAKE_TAIL:
						SnakeCell.drawStatic(batch, networkThing.getX(), networkThing.getY(), SnakeCellType.TAIL, networkThing.getDirection());
						break;
					case SNAKE_CORNER_LEFT:
						SnakeCell.drawStatic(batch, networkThing.getX(), networkThing.getY(), SnakeCellType.CORNER_LEFT, networkThing.getDirection());
						break;
					case SNAKE_CORNER_RIGHT:
						SnakeCell.drawStatic(batch, networkThing.getX(), networkThing.getY(), SnakeCellType.CORNER_RIGHT, networkThing.getDirection());
						break;
				}
			}
		}
	}
}
