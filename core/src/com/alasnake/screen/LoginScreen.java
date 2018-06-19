package com.alasnake.screen;

import com.alasnake.game.GameModelClient;
import com.alasnake.game.SpaceSnake;
import com.alasnake.net.SnakeClient;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import java.net.InetAddress;

/**
 * @author Alena Hanakova
 */
public class LoginScreen implements Screen {

	private SpaceSnake game;
	private SnakeClient client;
	private Stage stage;
	private TextField txfUserName;
	private TextField txfAddress;
	final TextButton btnServer;
	final TextButton btnClient;
	final TextButton btnStart;
	private boolean isServer;
	private int width;
	private int height;
	private Table screenTable;

	public LoginScreen(SpaceSnake game) {
		this.game = game;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		height = Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();

		client = new SnakeClient();
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

		btnServer = new TextButton("Server", skin);
		btnClient = new TextButton("Client", skin);
		btnStart = new TextButton("Start", skin);

		btnServer.getLabel().setFontScale(1.5f);
		btnServer.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent e, float x, float y, int pointer, int button) {

				setIsServer(true);
			}

			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				return true;
			}
		});

		btnClient.getLabel().setFontScale(1.5f);
		btnClient.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent e, float x, float y, int pointer, int button) {

				setIsServer(false);
			}

			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				return true;
			}
		});

		btnStart.setColor(Color.FOREST);
		btnStart.getLabel().setFontScale(2);
		btnStart.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
				btnStart.setText("Starting...");
				btnStart.setColor(128, 128, 128, 0.5f);
				btnStart.setDisabled(true);
				btnStartClicked();
			}

			@Override
			public boolean touchDown(InputEvent e, float x, float y, int pointer, int button) {
				return true;
			}
		});

		String userName = System.getProperty("user.name");
		txfUserName = new TextField(userName, skin);
		txfUserName.setMessageText("Username");
		txfUserName.setOnlyFontChars(true);
		txfUserName.setColor(Color.LIGHT_GRAY);

		txfAddress = new TextField("", skin);

		Table textFieldsTable = new Table(skin);
		textFieldsTable.setSize(width, height);
		textFieldsTable.top().pad(height / 11, 0, 0, 0);
		textFieldsTable.add(txfUserName).size(width / 2, height / 10).padBottom(height / 15);
		textFieldsTable.row();
		textFieldsTable.add(txfAddress).size(width / 2, height / 10).padBottom(height / 15);

		Table innerButtonTable = new Table();
		innerButtonTable.setSize(width / 2, height);
		innerButtonTable.add(btnServer).size(width / 5, height / 4).spaceRight(width / 10);
		innerButtonTable.add(btnClient).size(width / 5, height / 4);

		Table buttonSwitchTable = new Table();
		buttonSwitchTable.add(innerButtonTable);
		buttonSwitchTable.padBottom(height / 15);

		Table startBtnTable = new Table();
		startBtnTable.add(btnStart).size(width / 2, height / 5);

		screenTable = new Table(skin);
		screenTable.top().add(textFieldsTable);
		screenTable.setSize(width, height);
		screenTable.setPosition(0, 0);
		screenTable.setBackground(new TextureRegionDrawable(new TextureRegion(SpaceSnake.backgroundTexture)));
		screenTable.row();
		screenTable.add(buttonSwitchTable);
		screenTable.row();
		screenTable.add(startBtnTable);

		stage.addActor(screenTable);
		setIsServer(true);
	}

	public void setIsServer(boolean isServer) {
		this.isServer = isServer;
		if (isServer) {
			btnServer.setColor(1f, 0.4f, 0.4f, 1);
			btnClient.setColor(Color.LIGHT_GRAY);
			txfAddress.setColor(0.1f, 0.1f, 0.1f, 0.3f);

			txfAddress.setMessageText("Address - for client use only");
			txfAddress.setDisabled(true);
		} else {
			InetAddress inetAddress = client.discoverHost();
			if (inetAddress != null){
				txfAddress.setText(inetAddress.getHostAddress());
			}
			btnServer.setColor(Color.LIGHT_GRAY);
			btnClient.setColor(1f, 0.4f, 0.4f, 1);
			txfAddress.setColor(Color.LIGHT_GRAY);
			txfAddress.setDisabled(false);
			txfAddress.setMessageText("Insert address or click \"Client\" to find existing game");
		}
	}

	public void btnStartClicked() {
		if (isServer) {
			game.setScreen(new LobbyScreen(game, txfUserName.getText()));
		} else {
			client.setIpAddress(txfAddress.getText());
			client.setPlayerName(txfUserName.getText());
			client.connect();
			GameModelClient gameModelClient = new GameModelClient(game, txfUserName.getText(), client);
			gameModelClient.initGraphics();
			game.setScreen(gameModelClient);
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
