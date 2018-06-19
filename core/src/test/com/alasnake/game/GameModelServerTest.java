package com.alasnake.game;

//import org.junit.Assert;
//import org.junit.Test;

/**
 * @author Alena Hanakova
 */
public class GameModelServerTest {

	private static final int TEST_X = 10;
	private static final int TEST_Y = 10;

//	@Test
//	public void testCollisionLaserHitSnakeCell() {
//		GameModel gameModel = new GameModel(new SpaceSnake(), "testname");
//		SnakeCell snakeCell = new SnakeCell(gameModel, null, TEST_X, TEST_Y, Direction.RIGHT, SnakeCellType.HEAD);
//		Laser laser = new Laser(gameModel, Direction.RIGHT, TEST_X, TEST_Y);
//		GameModelServer.collideSnakeCellWithThing(snakeCell, laser);
//		Assert.assertTrue(snakeCell.getNextAction() == ActionFlag.RESET);
//		Assert.assertTrue(laser.getNextAction() == ActionFlag.RESET);
//	}
//
//	@Test
//	public void testCollisionSnakeCellEatsFood() {
//		GameModel gameModel = new GameModel(new SpaceSnake(), "testname");
//		SnakeCell snakeCell = new SnakeCell(gameModel, null, TEST_X, TEST_Y, Direction.RIGHT, SnakeCellType.HEAD);
//		Food food = new Food(gameModel, TEST_X, TEST_Y);
//		GameModelServer.collideSnakeCellWithThing(snakeCell, food);
//		Assert.assertTrue(snakeCell.getNextAction() == ActionFlag.GROW);
//		Assert.assertTrue(food.getNextAction() == ActionFlag.RESET);
//	}
//
//	@Test
//	public void testCollisionSnakeHeadCollideSnakeBody() {
//		GameModel gameModel = new GameModel(new SpaceSnake(), "testname");
//		SnakeCell snakeCellHead = new SnakeCell(gameModel, null, TEST_X, TEST_Y, Direction.RIGHT, SnakeCellType.HEAD);
//		SnakeCell snakeCellBody = new SnakeCell(gameModel, null, TEST_X, TEST_Y, Direction.RIGHT, SnakeCellType.BODY);
//		GameModelServer.collideSnakeCellWithThing(snakeCellHead, snakeCellBody);
//		Assert.assertTrue(snakeCellHead.getNextAction() == ActionFlag.RESET);
//	}
//
//	@Test
//	public void testCollisionSnakeHeadCollideSnakeHead() {
//		GameModel gameModel = new GameModel(new SpaceSnake(), "testname");
//		SnakeCell snakeCellHead1 = new SnakeCell(gameModel, null, TEST_X, TEST_Y, Direction.RIGHT, SnakeCellType.HEAD);
//		SnakeCell snakeCellHead2 = new SnakeCell(gameModel, null, TEST_X, TEST_Y, Direction.RIGHT, SnakeCellType.HEAD);
//		GameModelServer.collideSnakeCellWithThing(snakeCellHead1, snakeCellHead2);
//		Assert.assertTrue(snakeCellHead1.getNextAction() == ActionFlag.RESET);
//		Assert.assertTrue(snakeCellHead2.getNextAction() == ActionFlag.RESET);
//	}
}
