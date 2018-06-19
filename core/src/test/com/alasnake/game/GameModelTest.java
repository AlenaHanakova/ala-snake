package com.alasnake.game;

//import org.junit.Assert;
//import org.junit.Test;

/**
 * @author Alena Hanakova
 */
public class GameModelTest {

	private static final int TEST_X = 10;
	private static final int TEST_Y = 10;

//	@Test
//	public void testMapping() {
//		GameModel gameModel = new GameModel(new SpaceSnake(), "testname");
//		Laser laser = new Laser(gameModel, Direction.RIGHT, TEST_X, TEST_Y);
//		Thing thing = gameModel.whosThere(laser.getX(), laser.getY());
//		if (!(thing instanceof Laser)) {
//			Assert.fail();
//		}
//	}
//
//	@Test
//	public void testComplexMapping() {
//		GameModel gameModel = new GameModel(new SpaceSnake(), "testname");
//		Food food = new Food(gameModel, TEST_X, TEST_Y);
//		Laser laser = new Laser(gameModel, Direction.RIGHT, TEST_X, TEST_Y);
//		Thing thing = gameModel.whosThere(laser.getX(), laser.getY());
//		if (!(thing instanceof MultipleThing)) {
//			Assert.fail();
//		}
//		MultipleThing multipleThing = (MultipleThing) thing;
//		Assert.assertTrue(multipleThing.getNumberOfThings() == 2);
//	}
}
