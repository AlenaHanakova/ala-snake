package com.alasnake.game;

import com.alasnake.net.NetworkThing;
import com.alasnake.net.SnakeServer;
import com.alasnake.screen.FinishScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Alena Hanakova
 */
public class GameModelServer extends GameModel {

	private final SnakeServer server;
	private int shouldMove = 0;

	private List<Player> players = new ArrayList<Player>();
	private List<Color> colors;
	private Timer timer;
	private boolean pause;

	public GameModelServer(SpaceSnake game, String username, SnakeServer server) {
		super(game, username);
		this.server = server;
		server.setState(SnakeServer.STATE.GAME);
		Collections.shuffle(startPositions);

		colors = new ArrayList<Color>(NUMBER_OF_PLAYERS);
		colors.add(Color.BLUE);
		colors.add(Color.CORAL);
		colors.add(Color.RED);
		colors.add(Color.GOLD);

		Map<Integer, PlayerInfo> clientsList = server.getPlayerInfoList();
		List<Integer> idOfClients = new ArrayList<Integer>(clientsList.keySet());

		// create snake for me
		Snake snake = new Snake(this, startPositions.get(0), 0, colors.get(0), username);
		snakes.add(snake);
		Player player = new Player(this, snake, Player.Control.KEYBOARD);
		players.add(player);

		// create snakes form clients and rest for AI
		for (int i = 1; i < NUMBER_OF_PLAYERS; i++) {

			PlayerInfo playerInfo = null;
			if (clientsList.size() > i - 1) {
				playerInfo = clientsList.get(idOfClients.get(i - 1));
			}
			if (playerInfo != null) {
				snake = new Snake(this, startPositions.get(i), 0, colors.get(i), playerInfo.getName());
				snakes.add(snake);
				player = new Player(this, snake, Player.Control.CLIENT);
				server.putClient(idOfClients.get(i - 1), player);
			} else {
				snake = new Snake(this, startPositions.get(i), 0, colors.get(i), "AI-" + (i));
				snakes.add(snake);
				player = new Player(this, snake, Player.Control.AI);
			}
			players.add(player);
		}
		food = new Food(this, 5, 3);

		timer = new Timer();

		timer.scheduleTask(new Timer.Task() {

							   @Override
							   public void run() {
								   gameStep();
							   }
						   }, 1, 0.03f
		);
	}

	/**
	 * All objects are sent to the connected clients.
	 */
	private void sendAllObjectToClients() {
		List<NetworkThing> allNetworkThings = new ArrayList<NetworkThing>();
		for (Snake snake : snakes) {
			allNetworkThings.addAll(snake.getNetworkThings());
		}
		synchronized (lasers) {
			for (Laser laser : lasers) {
				allNetworkThings.add(laser.getNetworkThing());
			}
		}
		allNetworkThings.add(getFood().getNetworkThing());
		server.sendToAllTCP(allNetworkThings);
	}

	public void moveLasers() {
		synchronized (lasers) {
			for (Laser laser : lasers) {
				laser.move();
			}
		}
	}

	/**
	 * Move all objects in the game area.
	 */
	private void moveAllObjectsExceptLasers() {
		if (shouldMove == 3) {
			for (Player player : players) {
				player.move();
			}
			shouldMove = 0;
		} else {
			shouldMove++;
		}
	}

	/**
	 * All lasers do actions.
	 * This actions could be set in reaction to collisions.
	 */
	private void doActionsForLasers() {
		synchronized (lasers) {
			for (int i = 0; i < lasers.size(); i++) {
				lasers.get(i).doAction();
			}
		}
	}

	/**
	 * All objects do actions.
	 * This actions could be set in reaction to collisions.
	 */
	private void doActionsForObjects() {
		synchronized (lasers) {
			for (int i = 0; i < lasers.size(); i++) {
				lasers.get(i).doAction();
			}
		}
		for (int i = 0; i < snakes.size(); i++) {
			snakes.get(i).doAction();
		}
		food.doAction();
	}

	public void gameStep() {
		moveLasers();
		checkCollision();
		doActionsForLasers();
		moveAllObjectsExceptLasers();
		checkCollision();
		doActionsForObjects();
		sendAllObjectToClients();
	}

	/**
	 * Checks collisions in multiple things.
	 */
	private void checkCollision() {
		for (MultipleThing multipleThing : multipleThings) {
			if (multipleThing.getNumberOfThings() == 2) {
				checkCollision(multipleThing.getThingOnPosition(0), multipleThing.getThingOnPosition(1));
				checkCollision(multipleThing.getThingOnPosition(1), multipleThing.getThingOnPosition(0));
			}else{
				checkCollisionOfThreeAndMore(multipleThing);
			}
		}
		multipleThings = new ArrayList<MultipleThing>();
	}

	private static void checkCollision(Thing thing1, Thing thing2) {
		if (thing1 instanceof SnakeCell) {
			SnakeCell snakeCell1 = (SnakeCell) thing1;
			collideSnakeCellWithThing(snakeCell1, thing2);
		}
	}

	protected static void collideSnakeCellWithThing(SnakeCell snakeCell1, Thing thing2) {
		if (thing2 instanceof SnakeCell) {
			SnakeCell snakeCell2 = (SnakeCell) thing2;
			if (snakeCell1.isHead() && snakeCell2.isHead()) {
				snakeCell1.setNextAction(ActionFlag.RESET);
				snakeCell2.setNextAction(ActionFlag.RESET);
			} else if (snakeCell1.isHead()) {
				snakeCell1.setNextAction(ActionFlag.RESET);
			}
		} else if (thing2 instanceof Food) {
			Food food = (Food) thing2;
			snakeCell1.setNextAction(ActionFlag.GROW);
			food.setNextAction(ActionFlag.RESET);
		} else if (thing2 instanceof Laser) {
			snakeCell1.setNextAction(ActionFlag.RESET);
			thing2.setNextAction(ActionFlag.RESET);
		}
	}
	public void checkCollisionOfThreeAndMore(MultipleThing multipleThing){
		int snakes = 0;
		int lasers = 0;
		for (int i = 0; i <  multipleThing.getNumberOfThings(); i++) {
			if(multipleThing.getThingOnPosition(i) instanceof  SnakeCell){
				snakes ++;
		}else if(multipleThing.getThingOnPosition(i) instanceof  Laser)
				{
					lasers ++ ;
				}
			}
			if(snakes ==0)	return;
		else if(lasers > 0){
				for (int i = 0; i < multipleThing.getNumberOfThings(); i++) {
					multipleThing.getThingOnPosition(i).setNextAction(ActionFlag.RESET);
				}
			}
		else{
				for (int i = 0; i < multipleThing.getNumberOfThings(); i++) {
					if (multipleThing.getThingOnPosition(i) instanceof Food) {
						multipleThing.getThingOnPosition(i).setNextAction(ActionFlag.RESET);
					}else {
						SnakeCell snakeCell = (SnakeCell) multipleThing.getThingOnPosition(i);
						if (snakeCell.isHead()){
							snakeCell.setNextAction(ActionFlag.RESET);
						}
					}

				}

			}
		}



	@Override
	public void dispose() {
		stopGame();
		server.stop();
		super.dispose();
	}

	public void checkCollisionOnGivenField(int x, int y) {
		if (isWithinBoundsOfField(x, y)) {
			Thing thing = fieldOfThings[x][y];
			if (thing instanceof MultipleThing) {
				MultipleThing multipleThing = (MultipleThing) thing;
				checkCollision(multipleThing.getThingOnPosition(0), multipleThing.getThingOnPosition(1));
				checkCollision(multipleThing.getThingOnPosition(1), multipleThing.getThingOnPosition(0));
			}
			doActionsForLasers();
		}
	}

	public void stopGame() {
		if (timer != null) {
			timer.stop();
		}
	}

	public void startGame() {
		timer.start();
	}

	/**
	 * Stop or start the game (pause or unpause).
	 */
	public void pauseGame() {
		if (!pause) {
			stopGame();
			pause = true;
		} else {
			startGame();
			pause = false;
		}
	}

	/**
	 * Pause the game for the given milliseconds.
	 *
	 * @param time Time in milliseconds.
	 */
	public void pauseGame(int time) {
		timer.stop();
		timer.delay(time);
	}

	public void finish() {
		Statistics statistics = createStatistics(snakes);
		server.sendToAllTCP(statistics);
		dispose();
		game.setScreen(new FinishScreen(game, statistics));
	}

	public Statistics createStatistics(List<Snake> snakes) {
		int snakesSize = snakes.size();
		List<String> userNames = new ArrayList<String>();
		int[] sizes = new int[snakesSize];
		for (int i = 0; i < snakesSize; i++) {
			Snake snake = snakes.get(i);
			userNames.add(snake.getUserName());
			sizes[i] = snake.getSize();
		}
		return new Statistics(userNames, sizes);
	}
}




