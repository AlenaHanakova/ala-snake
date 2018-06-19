package com.alasnake.screen;

import com.alasnake.game.GameModelServer;
import com.alasnake.game.SpaceSnake;
import com.alasnake.net.SnakeServer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * @author Alena Hanakova
 */
public class LobbyScreen implements Screen {

	private final Stage stage;
	private final SpaceSnake game;
	private final TextButton buttonStart;
	private final String userName;
	private final SnakeServer server;
	public static String clients = "Waiting for clients... \n";
	private TextArea messages;

	public LobbyScreen(SpaceSnake game, String userName) {
		this.game = game;
		this.userName = userName;
		int height = Gdx.graphics.getHeight();
		int width = Gdx.graphics.getWidth();

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		messages = new TextArea(clients, skin);
		messages.setDisabled(true);
		;
		Table screenTable = new Table(skin);
		screenTable.setSize(width, height);
		screenTable.setPosition(0, 0);
		screenTable.setBackground(new TextureRegionDrawable(new TextureRegion(SpaceSnake.backgroundTexture)));

		buttonStart = new TextButton("Start", skin);
		stage.addActor(buttonStart);

		buttonStart.setSize(20, 20);
		buttonStart.setPosition(5, 10);
		buttonStart.getLabel().setFontScale(2);
		buttonStart.setColor(Color.FOREST);
		buttonStart.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
				buttonStart.setText("Starting...");
				buttonStart.setColor(128, 128, 128, 0.5f);
				buttonStart.setDisabled(true);
				buttonStartClicked();
			}

			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				return true;
			}
		});

		server = new SnakeServer();
		server.start();

		screenTable.add(messages).size(width / 2, height / 2).padBottom(height / 9);
		screenTable.row();
		screenTable.add(buttonStart).size(width / 2, height / 4);
		stage.addActor(screenTable);
	}

	public void buttonStartClicked() {
		GameModelServer gameModelServer = new GameModelServer(game, userName, server);
		gameModelServer.initGraphics();
		game.setScreen(gameModelServer);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		messages.setText(clients);
		stage.act(delta);
		stage.getBatch().begin();
		stage.getBatch().draw(SpaceSnake.backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.getBatch().end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
