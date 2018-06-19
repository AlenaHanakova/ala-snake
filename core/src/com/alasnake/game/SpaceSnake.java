package com.alasnake.game;

/**
 * @author Alena Hanakova
 */

import com.alasnake.screen.LoginScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceSnake extends Game {

	public static Texture backgroundTexture;
	public static Sprite backgroundSprite;
	private SpriteBatch batch;

	public void create() {
		backgroundTexture = new Texture(Gdx.files.internal("images/HadBackground.png"));
		backgroundSprite = new Sprite(backgroundTexture);
		batch = new SpriteBatch();
		setScreen(new LoginScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}
}